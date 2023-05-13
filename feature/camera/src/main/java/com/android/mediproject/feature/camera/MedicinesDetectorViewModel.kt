package com.android.mediproject.feature.camera

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.SurfaceHolder
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.aimodel.DetectedObject
import com.android.mediproject.feature.camera.aimodel.Yolo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    companion object {
        private var _yolo: Yolo? = null
        val yolo get() = _yolo
    }

    /**
     * 검출된 객체를 가져오는데 사용되는 SharedFlow
     *
     */
    private val _detactedObjects = MutableSharedFlow<List<DetectedObject>>(replay = 1, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)
    val detectedObjects = _detactedObjects.asSharedFlow()

    /**
     * 검출된 이미지를 가져오는데 사용되는 SharedFlow
     *
     */
    private val _detectedImage = MutableSharedFlow<Bitmap>(replay = 1, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)
    val detectedImage = _detectedImage.asSharedFlow()

    private val _loadedModel = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)
    val loadedModel = _loadedModel.asSharedFlow()

    /**
     * Yolo 모델을 로드한다.
     *
     */
    fun loadModel(assetManager: AssetManager) {
        viewModelScope.launch(ioDispatcher) {
            if (_yolo == null) {
                _yolo = Yolo()
                yolo?.loadModel(assetManager)?.apply {
                    _loadedModel.emit(this)
                }
            }
        }
    }

    /**
     * 검출된 객체를 가져오고 비트맵으로 변환한다.
     *
     * 이미지와 검출 객체 정보를 가져온 직후 카메라를 닫는다.
     *
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun retrieveObjects() {
        viewModelScope.launch(ioDispatcher) {
            yolo?.also { yolo ->
                val img = yolo.getCurrentImage()
                val objects = yolo.detectedObjects()

                yolo.closeCamera()
                _detectedImage.emit(toBitmap(img.base64, img.width, img.height))

                if (objects == null) {
                    _detactedObjects.emit(emptyList())
                } else {
                    // to Bitmap
                    objects.forEach { detectedObject ->
                        detectedObject.bitmap = toBitmap(detectedObject.base64, detectedObject.width, detectedObject.height)
                    }
                    _detactedObjects.emit(objects.toList())
                }
            }
        }
    }

    fun surfaceCreated(holder: SurfaceHolder) {
        viewModelScope.launch(ioDispatcher) { yolo?.setOutputWindow(holder.surface) }
    }

    fun openCamera() {
        viewModelScope.launch(ioDispatcher) { yolo?.openCamera(1) }
    }

    fun closeCamera() {
        viewModelScope.launch(ioDispatcher) { yolo?.closeCamera() }
    }


    /**
     * base64 문자열을 비트맵으로 변환한다.
     *
     * @param base64 base64 문자열
     */
    @OptIn(ExperimentalEncodingApi::class)
    private fun toBitmap(base64: String, width: Int, height: Int): Bitmap {
        var decodedBytes: ByteArray? = Base64.decode(
            base64.subSequence(0, base64.length), 0, base64.length
        )
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes!!.size, BitmapFactory.Options().apply {
            inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888
            outWidth = width
            outHeight = height
        })

        decodedBytes = null
        return bitmap
    }

    /**
     * 카메라를 닫고, 검출된 객체와 이미지를 초기화한다.
     */
    fun clear() {
        _yolo?.closeCamera()
        _detactedObjects.resetReplayCache()
        _detectedImage.resetReplayCache()
    }
}