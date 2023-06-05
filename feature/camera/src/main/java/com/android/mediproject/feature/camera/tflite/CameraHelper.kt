package com.android.mediproject.feature.camera.tflite

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.detector.Detection
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 카메라 제어, ai처리 담당
 */
@Singleton
class CameraHelper @Inject constructor(
    @ApplicationContext private val context: Context, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher) :
    LifecycleEventObserver, AiController, CameraController {

    private var _camera: Camera? = null

    private var _preview: Preview? = null

    private var _previewView: PreviewView? = null

    private var _cameraProvider: ProcessCameraProvider? = null
    private val cameraProvider get() = _cameraProvider!!

    private var _objectDetector: ObjectDetector? = null
    private val objectDetector get() = _objectDetector!!


    private val _detectionResult =
        MutableSharedFlow<List<Detection>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 5)

    override val detectionResult get() = _detectionResult.asSharedFlow()

    // 약 검출 결과 콜백
    override var detectionCallback: OnDetectionCallback? = null

    private var _bitmapBuffer: Bitmap? = null
    private var bitmapBuffer: Bitmap
        get() = _bitmapBuffer!!
        set(value) {
            _bitmapBuffer?.recycle()
            _bitmapBuffer = value
        }

    // 카메라 처리 스레드
    private var _cameraExecutor: ExecutorService? = null
    private var cameraExecutor: ExecutorService
        get() = _cameraExecutor!!
        set(value) {
            if (_cameraExecutor != null) _cameraExecutor?.shutdown()
            _cameraExecutor = value
        }


    // Fragment의 생명주기에 맞춰 필요한 로직을 처리
    private var _fragmentLifeCycleOwner: LifecycleOwner? = null
    override var fragmentLifeCycleOwner: LifecycleOwner
        get() = _fragmentLifeCycleOwner!!
        set(value) {
            _fragmentLifeCycleOwner = value
            value.lifecycle.addObserver(this)
        }

    // activity 생명주기에 맞춰 필요한 로직을 처리
    private var _activityLifecycle: Lifecycle? = null
    override var activityLifecycle: Lifecycle
        get() = _activityLifecycle!!
        set(value) {
            _activityLifecycle = value
            value.addObserver(this)
        }


    /**
     * TFLite 모델을 로드한다.
     */
    override suspend fun loadModel(): Result<Unit> = suspendCoroutine { continuation ->
        try {
            TfLiteVision.initialize(context).addOnCompleteListener {

                if (it.isSuccessful) {
                    val modelFile = context.assets.open("automl_tflite.tflite")

                    val objectDetectorOptions = ObjectDetector.ObjectDetectorOptions.builder().setMaxResults(8).setScoreThreshold(0.3f)
                        .setBaseOptions(BaseOptions.builder().apply {
                            useNnapi()
                        }.build()).build()

                    val bytes = modelFile.readBytes()
                    val byteBuffer = ByteBuffer.allocateDirect(bytes.size)
                    byteBuffer.order(ByteOrder.nativeOrder())
                    byteBuffer.put(bytes)

                    _objectDetector?.close()
                    _objectDetector = ObjectDetector.createFromBufferAndOptions(byteBuffer, objectDetectorOptions)

                    continuation.resume(Result.success(Unit))
                } else {
                    continuation.resume(Result.failure(it.exception?.cause ?: Exception("Failed to load model")))
                }
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }


    private fun detect(image: ImageProxy) {
        image.use {
            bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)
        }

        val imageProcessor = ImageProcessor.Builder().add(Rot90Op((-image.imageInfo.rotationDegrees) / 90)).build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))

        detectionCallback?.onDetectedResult(objectDetector.detect(tensorImage).apply {
            _detectionResult.tryEmit(this)
        }, tensorImage.width, tensorImage.height)
    }

    // fragment view의 생명주기 변화 수신
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                if (source == _fragmentLifeCycleOwner) {
                    _fragmentLifeCycleOwner?.lifecycle?.removeObserver(this)
                    _detectionResult.resetReplayCache()
                    _cameraProvider?.unbindAll()
                    _bitmapBuffer?.recycle()
                    _cameraExecutor?.shutdown()

                    _preview = null
                    _camera = null
                    _cameraProvider = null
                    _bitmapBuffer = null
                } else {
                    _activityLifecycle?.removeObserver(this)
                    _objectDetector?.close()
                    _objectDetector = null
                }
            }

            Lifecycle.Event.ON_START -> {

            }

            else -> {

            }

        }
    }

    /**
     * 카메라를 초기화한다.
     */
    override suspend fun setupCamera(previewView: PreviewView): Result<Unit> = suspendCoroutine { continuation ->
        try {
            _previewView = previewView
            _cameraExecutor = Executors.newSingleThreadExecutor()

            ProcessCameraProvider.getInstance(context).also { cameraProviderFuture ->
                cameraProviderFuture.addListener(Runnable {

                    val cameraSelector =
                        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

                    _cameraProvider = cameraProviderFuture.get()
                    val imageAnalyzer = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3).setTargetRotation(previewView.display.rotation)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build().also {
                            it.setAnalyzer(cameraExecutor) { image ->
                                if (_bitmapBuffer == null) _bitmapBuffer =
                                    Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
                                detect(image)
                            }
                        }

                    _preview = Preview.Builder().setTargetAspectRatio(RATIO_4_3).setTargetRotation(previewView.display.rotation).build()
                    cameraProvider.unbindAll()

                    _camera =
                        cameraProvider.bindToLifecycle(fragmentLifeCycleOwner, cameraSelector, _preview, imageAnalyzer)
                    _preview?.setSurfaceProvider(previewView.surfaceProvider)

                    Log.d("ProcessCameraProvider", "카메라 바인딩 성공")
                    continuation.resume(Result.success(Unit))
                }, ContextCompat.getMainExecutor(context))
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }

    override fun pause() {
        _preview?.setSurfaceProvider(null)
    }

    override fun resume() {
        _preview?.setSurfaceProvider(_previewView?.surfaceProvider)
    }


    fun interface OnDetectionCallback {
        fun onDetectedResult(objects: List<Detection>, width: Int, height: Int)
    }
}

/**
 * AI를 제어하는 인터페이스
 */
interface AiController {
    val detectionResult: SharedFlow<List<Detection>>

    suspend fun loadModel(): Result<Unit>
}

/**
 * 카메라를 제어하는 인터페이스
 */
interface CameraController {
    fun pause()
    fun resume()
    suspend fun setupCamera(previewView: PreviewView): Result<Unit>
    var detectionCallback: CameraHelper.OnDetectionCallback?

    var fragmentLifeCycleOwner: LifecycleOwner
    var activityLifecycle: Lifecycle
}