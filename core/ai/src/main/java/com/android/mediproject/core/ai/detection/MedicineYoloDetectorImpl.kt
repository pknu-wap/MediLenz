package com.android.mediproject.core.ai.detection

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtLoggingLevel
import ai.onnxruntime.OrtSession
import ai.onnxruntime.extensions.OrtxPackage
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.util.Log
import android.util.Size
import androidx.core.graphics.scale
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.model.DetectionResultEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.newFixedThreadPoolContext
import java.lang.ref.WeakReference
import java.nio.FloatBuffer
import java.util.Collections
import java.util.PriorityQueue

object MedicineYoloDetectorImpl : MedicineDetector {
    private const val BATCH_SIZE = 1
    private const val PIXEL_SIZE = 3
    private const val INPUT_SIZE = 640
    private const val STD = 255.0f
    private const val BITMAP_SIZE = INPUT_SIZE * INPUT_SIZE
    private const val CONFIDENCE = 0.6f
    private const val IOU = 0.5f
    private const val CLASS_NUM = 80

    private val inputShape = longArrayOf(
        BATCH_SIZE.toLong(), PIXEL_SIZE.toLong(), INPUT_SIZE.toLong(), INPUT_SIZE.toLong(),
    )

    private val buffer = FloatBuffer.allocate(BATCH_SIZE * PIXEL_SIZE * INPUT_SIZE * INPUT_SIZE)

    private val bitmapPixels = IntArray(BITMAP_SIZE)     // 한 장의 사진에 대한 픽셀을 담을 배열

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    override val aiModelState = _aiModelState.asStateFlow()

    private var _classes: Array<String>? = null
    private val classes get() = _classes!!

    private val ortEnv = OrtEnvironment.getEnvironment(OrtLoggingLevel.ORT_LOGGING_LEVEL_INFO)

    private var _ortSession: OrtSession? = null
    private val ortSession get() = _ortSession!!

    private const val greenIndex = BITMAP_SIZE
    private const val blueIndex = greenIndex + BITMAP_SIZE

    @OptIn(DelicateCoroutinesApi::class) private val calculatorDispatcher = newFixedThreadPoolContext(3, "calculaterDispatcher")

    private enum class RGB(val converter: (Int) -> Int, val floatBuffer: FloatBuffer) {
        RED(
            {
                it shr 16 and 0xFF
            },
            FloatBuffer.allocate(BATCH_SIZE * INPUT_SIZE * INPUT_SIZE),
        ),
        GREEN(
            {
                it shr 8 and 0xFF
            },
            FloatBuffer.allocate(BATCH_SIZE * INPUT_SIZE * INPUT_SIZE),
        ),
        BLUE(
            {
                it and 0xFF
            },
            FloatBuffer.allocate(BATCH_SIZE * INPUT_SIZE * INPUT_SIZE),
        )
    }

    private val rgbs = RGB.values()
    private val classIndexRange = 0 until CLASS_NUM

    override fun release() {

    }

    @Suppress("UNCHECKED_CAST")
    override fun detect(bitmap: Bitmap): Result<DetectionResultEntity> {
        var cuttedHeight: Int

        val scaledBitmap = WeakReference(
            bitmap.let { bitmap ->
                val width = bitmap.width
                val top = (bitmap.height - width) / 2

                cuttedHeight = top / 2
                Bitmap.createBitmap(bitmap, 0, top, width, width).scale(INPUT_SIZE, INPUT_SIZE)
            },
        ).get()!!

        val floatBuffer = scaledBitmap.floatBuffer()

        val inputTensor = WeakReference(OnnxTensor.createTensor(ortEnv, floatBuffer, inputShape)).get()!!
        val outputTensor = WeakReference(ortSession.run(Collections.singletonMap(ortSession.inputNames.iterator().next(), inputTensor))).get()!!

        val output1: Array<FloatArray> = (outputTensor.get(0).value as Array<Array<FloatArray>>)[0]
        val output2: Array<FloatArray> = (outputTensor.get(1).value as Array<Array<FloatArray>>)[0]

        inputTensor.close()
        outputTensor.close()

        val start = System.currentTimeMillis()
        val results = classify(output1, output2)
        Log.d("wap", "outputToPredict: ${System.currentTimeMillis() - start}ms")

        return Result.success(
            DetectionResultEntity(
                inferencedImageSize = Size(bitmap.width, bitmap.height),
                items = results.map {
                    DetectionResultEntity.Item(
                        boundingBox = it.rectF.run {
                            RectF(
                                left.position(INPUT_SIZE) * bitmap.width,
                                top.position(INPUT_SIZE) * bitmap.height + cuttedHeight,
                                right.position(INPUT_SIZE) * bitmap.width,
                                bottom.position(INPUT_SIZE) * bitmap.height - cuttedHeight,
                            )
                        },
                        label = classes[it.classIndex],
                        confidence = (it.score * 100f).toInt(),
                    )
                },
            ),
        )
    }

    private fun Bitmap.floatBuffer(): FloatBuffer {
        buffer.rewind()
        getPixels(bitmapPixels, 0, width, 0, 0, width, height)

        var idx: Int
        var pixelValue: Int
        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                idx = INPUT_SIZE * i + j
                pixelValue = bitmapPixels[idx]

                buffer.put(idx, Color.red(pixelValue) / STD)
                buffer.put(idx + greenIndex, Color.green(pixelValue) / STD)
                buffer.put(idx + blueIndex, Color.blue(pixelValue) / STD)
            }
        }

        buffer.rewind()
        return buffer
    }

    private fun classify(output1: Array<FloatArray>, output2: Array<FloatArray>): List<Res> {
        val results = mutableMapOf<Int, MutableList<Res>>()

        output1.zip(output2).forEachIndexed { i, pair ->
            // 라벨들 중 가장 높은 확률을 가진 라벨 및 확률
            pair.second.withIndex().filter { it.value >= CONFIDENCE }.run {
                if (isNotEmpty()) {
                    maxBy { it.value }.let { (classIndex, conf) ->
                        // 사각형은 화면 밖을 나갈 수 없으니 넘기면 최대치로 변경
                        pair.first.run {
                            val rectF = RectF(
                                maxOf(0f, this[0]), maxOf(0f, this[1]),
                                minOf(INPUT_SIZE - 1f, this[2]), minOf(INPUT_SIZE - 1f, this[3]),
                            )
                            if (results[classIndex] == null) results[classIndex] = mutableListOf()
                            results[classIndex]!!.add(Res(classIndex, conf, rectF))
                        }
                    }
                }
            }
        }
        return nms(results)
    }

    // 비 최대 억제 (nms)
    private fun nms(res: Map<Int, List<Res>>): List<Res> {
        val finalList = mutableListOf<Res>()
        val pq = PriorityQueue<Res> { o1, o2 ->
            o1.score.compareTo(o2.score)
        }

        for (classId in res.keys) {
            pq.addAll(res[classId]!!)

            //NMS 처리
            while (pq.isNotEmpty()) {
                // 큐 안에 속한 최대 확률값을 가진 class 저장
                val detections = pq.toTypedArray()
                val max = detections[0]
                finalList.add(max)
                pq.clear()

                // 교집합 비율 확인하고 50%넘기면 제거
                for (k in 1 until detections.size) {
                    val detection = detections[k]
                    val rectF = detection.rectF

                    if (boxIOU(max.rectF, rectF) < IOU) {
                        pq.add(detection)
                    }
                }
            }
        }
        return finalList
    }

    // 겹치는 비율 (교집합/합집합)
    private inline fun boxIOU(a: RectF, b: RectF): Float {
        return boxIntersection(a, b) / boxUnion(a, b)
    }

    private fun boxIntersection(a: RectF, b: RectF): Float {
        val w = overlap(
            (a.left + a.right) / 2f, a.right - a.left,
            (b.left + b.right) / 2f, b.right - b.left,
        )
        val h = overlap(
            (a.top + a.bottom) / 2f, a.bottom - a.top,
            (b.top + b.bottom) / 2f, b.bottom - b.top,
        )

        return if (w < 0 || h < 0) 0f else w * h
    }

    private fun boxUnion(a: RectF, b: RectF): Float {
        val i = boxIntersection(a, b)
        return (a.right - a.left) * (a.bottom - a.top) + (b.right - b.left) * (b.bottom - b.top) - i
    }

    private fun overlap(x1: Float, w1: Float, x2: Float, w2: Float): Float {
        val left = maxOf(x1 - w1 / 2, x2 - w2 / 2)
        val right = minOf(x1 + w1 / 2, x2 + w2 / 2)
        return right - left
    }

    override suspend fun initialize(context: Context): Result<Unit> {
        _aiModelState.value = AiModelState.Loading
        return try {
            context.assets.open("class2.txt").bufferedReader().use { reader ->
                _classes = reader.readLines().toTypedArray()
            }

            val modelFileName = "yolo_nas_s.onnx"

            /**
            val modelFilePath = "${context.filesDir.absolutePath}/$modelFileName"

            val outputFile = File(modelFilePath)
            context.assets.open(modelFileName).use { inputStream ->
            FileOutputStream(outputFile).use { outputStream ->
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
            }
            }
            }
             */

            val sessionOption = OrtSession.SessionOptions().apply {
                setIntraOpNumThreads((Runtime.getRuntime().availableProcessors() - 2).coerceAtLeast(1))
                registerCustomOpLibrary(OrtxPackage.getLibraryPath())
            }

            val bytes = context.assets.open(modelFileName).readBytes()
            _ortSession = ortEnv.createSession(
                bytes, sessionOption,
            )
            _aiModelState.value = AiModelState.Loaded
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            _aiModelState.value = AiModelState.LoadFailed
            Result.failure(Exception("Failed to initialize objectDetector"))
        }
    }

    private fun Float.position(size: Int): Float = this / size

    private data class Res(val classIndex: Int, val score: Float, val rectF: RectF)
}
