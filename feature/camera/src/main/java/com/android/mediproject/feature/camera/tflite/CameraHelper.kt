package com.android.mediproject.feature.camera.tflite

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.AspectRatio.RATIO_4_3
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.detector.Detection
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newSingleThreadExecutor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

/**
 * 카메라 제어, ai처리 담당
 */
class CameraHelper @Inject constructor() : ContentProvider(), LifecycleEventObserver, AiController, CameraController {


    companion object {
        @set:Synchronized @get:Synchronized private var _instance: CameraHelper? = null
            set(value) {
                if (field == null) field = value
            }
        val instance: CameraHelper get() = _instance!!

        private val mCameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        @get:Synchronized @set:Synchronized private var _objectDetector: ObjectDetector? = null
        private val objectDetector get() = _objectDetector!!
    }

    init {
        _instance = this
    }

    private var _mPreview: Preview? = null
    private val mPreview get() = _mPreview!!
    private var _mPreviewView: PreviewView? = null
    private val mPreviewView get() = _mPreviewView!!
    private var _mImageAnalyzer: ImageAnalysis? = null
    private val mImageAnalyzer get() = _mImageAnalyzer!!

    private var _mCameraProvider: ProcessCameraProvider? = null
    private val mCameraProvider get() = _mCameraProvider!!

    private var _bitmapBuffer: Bitmap? = null
        set(value) {
            field?.recycle()
            field = value
        }

    private val bitmapBuffer get() = _bitmapBuffer!!

    // 카메라 처리 스레드
    private var cameraExecutor: ExecutorService? = null

    private var _fragmentLifeCycleOwner: LifecycleOwner by Delegates.notNull()

    override var fragmentLifeCycleOwner: LifecycleOwner
        set(value) {
            _fragmentLifeCycleOwner = value
            value.lifecycle.addObserver(this)
        }
        get() = _fragmentLifeCycleOwner


    // 검출 결과
    private val _detectionResult =
        MutableSharedFlow<List<Detection>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 10)

    override val detectionResult get() = _detectionResult.asSharedFlow()

    // 약 검출 결과 콜백
    override var detectionCallback: OnDetectionCallback by Delegates.notNull()

    private fun loadModel(@ApplicationContext context: Context) {
        try {
            TfLiteVision.initialize(context).addOnCompleteListener {
                if (it.isSuccessful) {
                    val modelFile = WeakReference(context.assets.open("automl_tflite3.tflite")).get()!!
                    val objectDetectorOptions = ObjectDetector.ObjectDetectorOptions.builder().setMaxResults(10).setScoreThreshold(0.4f)
                        .setBaseOptions(
                            BaseOptions.builder().apply {
                                useNnapi()
                            }.build(),
                        ).build()

                    val bytes = WeakReference(modelFile.readBytes()).get()!!
                    val byteBuffer = ByteBuffer.allocateDirect(bytes.size)
                    byteBuffer.order(ByteOrder.nativeOrder())
                    byteBuffer.put(bytes)

                    _objectDetector = ObjectDetector.createFromBufferAndOptions(byteBuffer, objectDetectorOptions)
                } else {
                    it.exception?.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun detect(image: ImageProxy) {
        val imageProcessor = ImageProcessor.Builder().add(Rot90Op((-image.imageInfo.rotationDegrees) / 90)).build()
        WeakReference(imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))).get()?.also { tensorImage ->
            with(objectDetector.detect(tensorImage)) {
                detectionCallback.onDetectedResult(this, tensorImage.width, tensorImage.height)
                _detectionResult.tryEmit(this)
            }
        }
    }

    // fragment view의 생명주기 변화 수신
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                _mCameraProvider?.unbindAll()
                _bitmapBuffer?.recycle()
                _detectionResult.resetReplayCache()
                cameraExecutor?.shutdown()

                _mPreview = null
                _mPreviewView = null
                _mCameraProvider = null
                _mImageAnalyzer = null
                _bitmapBuffer = null
                cameraExecutor = null
            }

            else -> {
            }

        }
    }

    override suspend fun setupCamera(previewView: PreviewView): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            _mPreviewView = previewView

            if (_objectDetector == null) {
                runCatching {
                    withTimeout(5000L) {
                        while (true) {
                            if (_objectDetector != null) break
                            delay(100)
                        }
                    }
                }.onFailure {
                    return@withContext Result.failure(Exception("Failed to initialize objectDetector"))
                }
            }

            suspendCoroutine { continuation ->
                context?.also { context ->
                    try {
                        ProcessCameraProvider.getInstance(context).also { cameraProviderFuture ->
                            cameraProviderFuture.addListener(
                                kotlinx.coroutines.Runnable {
                                    _mCameraProvider = cameraProviderFuture.get()
                                    camera(true)
                                    continuation.resume(Result.success(Unit))
                                },
                                ContextCompat.getMainExecutor(context),
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continuation.resume(Result.failure(e))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun camera(start: Boolean) {
        if (start) {
            cameraExecutor = newSingleThreadExecutor()
            _mImageAnalyzer = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).setTargetAspectRatio(RATIO_4_3)
                .setTargetRotation(mPreviewView.display.rotation).setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
            mImageAnalyzer.setAnalyzer(cameraExecutor!!) { image ->
                if (_bitmapBuffer == null) _bitmapBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
                image.use { bitmapBuffer.copyPixelsFromBuffer(it.planes[0].buffer) }
                detect(image)
            }

            _mPreview = Preview.Builder().setTargetAspectRatio(RATIO_4_3).setTargetRotation(mPreviewView.display.rotation).build()
            mCameraProvider.bindToLifecycle(fragmentLifeCycleOwner, mCameraSelector, mPreview, mImageAnalyzer)
            mPreview.setSurfaceProvider(mPreviewView.surfaceProvider)
        } else {
            mImageAnalyzer.clearAnalyzer()
            mCameraProvider.unbindAll()
            mPreview.setSurfaceProvider(null)

            cameraExecutor?.shutdown()
            _mPreview = null
            cameraExecutor = null
            _mImageAnalyzer = null
        }
    }

    override fun pause() {
        camera(false)
    }

    override fun resume() {
        camera(true)
    }

    fun interface OnDetectionCallback {
        fun onDetectedResult(objects: List<Detection>, width: Int, height: Int)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(): Boolean {
        GlobalScope.launch(Dispatchers.Default) {
            context?.run {
                loadModel(this)
            }
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? =
        null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
