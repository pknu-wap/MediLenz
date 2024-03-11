package com.android.mediproject.core.ai.detection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Size
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.model.DetectionResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object MedicineDetectorImpl : MedicineDetector {

    private var _objectDetector: ObjectDetector? = null
    private val objectDetector get() = _objectDetector!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    override val aiModelState = _aiModelState.asStateFlow()

    private val imageProcessor = ImageProcessor.Builder().add(Rot90Op(0)).build()
    private val objectDetectorOptions = ObjectDetector.ObjectDetectorOptions.builder()
        .setMaxResults(10).setScoreThreshold(0.35f).setBaseOptions(
            BaseOptions.builder().useNnapi().build(),
        ).build()

    override fun release() {

    }

    private fun RectF.correct(imageSize: Size): RectF = RectF(
        left.coerceAtLeast(0f).coerceAtMost(imageSize.width.toFloat()),
        top.coerceAtLeast(0f).coerceAtMost(imageSize.height.toFloat()),
        right.coerceAtLeast(0f).coerceAtMost(imageSize.width.toFloat()),
        bottom.coerceAtLeast(0f).coerceAtMost(imageSize.height.toFloat()),
    )


    override fun detect(bitmap: Bitmap): Result<DetectionResultEntity> {
        val tensorImage = WeakReference(imageProcessor.process(TensorImage.fromBitmap(bitmap))).get()!!
        val imageSize = Size(
            tensorImage.width,
            tensorImage.height,
        )

        val items = objectDetector.detect(tensorImage).map { detection ->
            val (confidence, label) = detection.categories.maxBy { it.score }.run { (score * 100f).toInt() to label }
            DetectionResultEntity.Item(detection.boundingBox.correct(imageSize), label, confidence)
        }

        println("items: $items")

        return Result.success(
            DetectionResultEntity(
                imageSize,
                items,
            ),
        )
    }

    override suspend fun initialize(context: Context) = suspendCoroutine<Result<Unit>> { cont ->
        _aiModelState.value = AiModelState.Loading
        try {
            val modelFile = WeakReference(context.assets.open("automl_tflite3.tflite")).get()!!
            val byteBuffer = WeakReference(modelFile.readBytes()).get()!!.let { bytes ->
                ByteBuffer.allocateDirect(bytes.size).apply {
                    order(ByteOrder.nativeOrder())
                    put(bytes)
                }
            }

            _objectDetector = ObjectDetector.createFromBufferAndOptions(byteBuffer, objectDetectorOptions)
            _aiModelState.value = AiModelState.Loaded
            cont.resume(Result.success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            _aiModelState.value = AiModelState.LoadFailed
            cont.resume(Result.failure(Exception("Failed to initialize objectDetector")))
        }
    }
}
