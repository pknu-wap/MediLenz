package com.android.mediproject.feature.camera.tflite.camera

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        private const val TARGET_WIDTH = 320
        private const val TARGET_HEIGHT = 320

        @set:Synchronized @get:Synchronized private var _instance: CameraHelper? = null
            set(value) {
                if (field == null) field = value
            }
        val instance: CameraHelper get() = _instance!!

        private val mCameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        @get:Synchronized @set:Synchronized private var _objectDetector: ObjectDetector? = null
        private val objectDetector get() = _objectDetector!!

        private var modelLoadJob: Job? = null
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


    // 약 검출 결과 콜백
    override var detectionCallback: ObjDetectionCallback by Delegates.notNull()

    private suspend fun loadModel(@ApplicationContext context: Context) = suspendCoroutine<Result<Unit>> { cont ->
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
                    cont.resume(Result.success(Unit))
                } else {
                    it.exception?.printStackTrace()
                    cont.resume(Result.failure(Exception("Failed to initialize objectDetector")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cont.resume(Result.failure(Exception("Failed to initialize objectDetector")))
        }
    }

    override suspend fun getObjectDetectorStatus(): Result<Unit> = if (_objectDetector == null) {
        modelLoadJob?.join()
        if (_objectDetector == null) {
            Result.failure(Exception("Failed to initialize objectDetector"))
        } else {
            Result.success(Unit)
        }
    } else {
        Result.success(Unit)
    }

    // fragment view의 생명주기 변화 수신
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                _mImageAnalyzer?.clearAnalyzer()
                _mCameraProvider?.unbindAll()
                _bitmapBuffer?.recycle()
                _mPreview?.setSurfaceProvider(null)
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

    override suspend fun connectCamera(previewView: PreviewView): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            _mPreviewView = previewView
            suspendCoroutine { continuation ->
                context?.let { context ->
                    try {
                        ProcessCameraProvider.getInstance(context).run {
                            addListener(
                                kotlinx.coroutines.Runnable {
                                    _mCameraProvider = get()
                                    connectCamera()
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

    private fun connectCamera() {
        cameraExecutor = newSingleThreadExecutor()
        _mImageAnalyzer = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(mPreviewView.display.rotation).setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
        mImageAnalyzer.setAnalyzer(cameraExecutor!!) { image ->
            if (_bitmapBuffer == null) _bitmapBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            image.use {
                bitmapBuffer.copyPixelsFromBuffer(it.planes[0].buffer)
            }
            detect(image)
        }

        _mPreview = Preview.Builder().setTargetRotation(mPreviewView.display.rotation).build()
        mCameraProvider.bindToLifecycle(fragmentLifeCycleOwner, mCameraSelector, mPreview, mImageAnalyzer)
        mPreview.setSurfaceProvider(mPreviewView.surfaceProvider)
    }

    private fun detect(image: ImageProxy) {
        val imageProcessor = ImageProcessor.Builder().add(Rot90Op((-image.imageInfo.rotationDegrees) / 90)).build()
        WeakReference(imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))).get()?.let { tensorImage ->
            detectionCallback.onDetect(objectDetector.detect(tensorImage), tensorImage.width, tensorImage.height)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(): Boolean {
        modelLoadJob = GlobalScope.launch(Dispatchers.IO) {
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

    fun interface ObjDetectionCallback {
        fun onDetect(objects: List<Detection>, width: Int, height: Int)
    }
}
