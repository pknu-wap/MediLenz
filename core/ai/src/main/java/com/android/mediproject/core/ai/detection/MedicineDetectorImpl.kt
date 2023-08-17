package com.android.mediproject.core.ai.detection

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.ImageProxy
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.model.DetectionResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector
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

    override fun release() {

    }

    override fun detect(image: ImageProxy, bitmapBuffer: Bitmap): Result<DetectionResultEntity> {
        val imageProcessor = ImageProcessor.Builder().add(Rot90Op((-image.imageInfo.rotationDegrees) / 90)).build()
        val tensorImage = WeakReference(imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))).get()!!
        val items = objectDetector.detect(tensorImage).map {
            DetectionResultEntity.Item(it.boundingBox)
        }
        return Result.success(
            DetectionResultEntity(
                Size(
                    tensorImage.width,
                    tensorImage.height,
                ),
                items,
            ),
        )
    }

    override suspend fun initialize(context: Context) = suspendCoroutine<Result<Unit>> { cont ->
        _aiModelState.value = AiModelState.Loading
        try {
            TfLiteVision.initialize(context).addOnCompleteListener {
                if (it.isSuccessful) {
                    val modelFile = WeakReference(context.assets.open("automl_tflite3.tflite")).get()!!
                    val objectDetectorOptions = ObjectDetector.ObjectDetectorOptions.builder().setMaxResults(10).setScoreThreshold(0.4f)
                        .setBaseOptions(
                            BaseOptions.builder().useNnapi().build(),
                        ).build()

                    val bytes = WeakReference(modelFile.readBytes()).get()!!
                    val byteBuffer = ByteBuffer.allocateDirect(bytes.size)
                    byteBuffer.order(ByteOrder.nativeOrder())
                    byteBuffer.put(bytes)

                    _objectDetector = ObjectDetector.createFromBufferAndOptions(byteBuffer, objectDetectorOptions)
                    _aiModelState.value = AiModelState.Loaded
                    cont.resume(Result.success(Unit))
                } else {
                    it.exception?.printStackTrace()
                    _aiModelState.value = AiModelState.LoadFailed
                    cont.resume(Result.failure(Exception("Failed to initialize objectDetector")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _aiModelState.value = AiModelState.LoadFailed
            cont.resume(Result.failure(Exception("Failed to initialize objectDetector")))
        }
    }
}
