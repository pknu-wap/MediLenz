package com.android.mediproject.core.ai.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
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
import com.android.mediproject.core.ai.detection.MedicineDetector
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newSingleThreadExecutor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

/**
 * 카메라 제어, ai처리 담당
 */
@Singleton
class CameraHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val medicineDetector: MedicineDetector,
) : LifecycleEventObserver, CameraController {

    private val mCameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

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
    override var detectionCallback: OnDetectionListener by Delegates.notNull()


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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun connectCamera() {
        cameraExecutor = newSingleThreadExecutor()
        _mImageAnalyzer =
            ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).setTargetRotation(mPreviewView.display.rotation)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
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

    private fun detect(imageProxy: ImageProxy) {
        medicineDetector.detect(imageProxy, bitmapBuffer).onSuccess { detectionResultEntity ->
            detectionCallback.onDetect(
                detectionResultEntity.items.map {
                    OnDetectionListener.Object(
                        it.boundingBox,
                        it.confidence,
                        it.label,
                    )
                },
                detectionResultEntity.inferencedImageSize.width, detectionResultEntity.inferencedImageSize.height,
            )
        }
    }

    fun interface OnDetectionListener {
        fun onDetect(objects: List<Object>, capturedImageWidth: Int, capturedImageHeight: Int)

        data class Object(
            val boundingBox: RectF,
            val confidence: Int,
            val label: String,
        )
    }
}
