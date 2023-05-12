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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val yolo = MutableStateFlow(Yolo()).asStateFlow()
    private val _detactedObjects = MutableSharedFlow<List<DetectedObject>>(replay = 1, extraBufferCapacity = 1)
    val detectedObjects = _detactedObjects.asSharedFlow()

    private val _detectedImage = MutableSharedFlow<Bitmap>(replay = 1, extraBufferCapacity = 1)
    val detectedImage = _detectedImage.asSharedFlow()

    fun loadModel(assetManager: AssetManager) {
        viewModelScope.launch(ioDispatcher) {
            yolo.value.loadModel(assetManager)
        }
    }

    /**
     * 검출된 객체를 가져오고 비트맵으로 변환한다.
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun getDetectedObjects() {
        viewModelScope.launch(ioDispatcher) {
            val img = yolo.value.getCurrentImage()
            val objects = yolo.value.detectedObjects()

            yolo.value.closeCamera()
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

    fun surfaceCreated(holder: SurfaceHolder) {
        viewModelScope.launch(ioDispatcher) { yolo.value.setOutputWindow(holder.surface) }
    }

    fun openCamera() {
        viewModelScope.launch(ioDispatcher) { yolo.value.openCamera(1) }
    }

    fun closeCamera() {
        viewModelScope.launch(ioDispatcher) { yolo.value.closeCamera() }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun clear() {
        //  _detactedObjects.resetReplayCache()
        //  _detectedImage.resetReplayCache()
    }

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
}