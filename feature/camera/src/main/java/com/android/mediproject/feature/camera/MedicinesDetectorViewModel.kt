package com.android.mediproject.feature.camera

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.view.SurfaceHolder
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.ai.DetectedObject
import com.android.mediproject.feature.camera.ai.Yolo
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
            val result = yolo.value.detectedObjects()
            yolo.value.closeCamera()

            if (result == null) {
                _detactedObjects.emit(emptyList())
            } else {
                // to Bitmap
                result.map { detectedObject ->
                    var decodedBytes: ByteArray? = Base64.decode(
                        detectedObject.base64.subSequence(0, detectedObject.base64.length), 0, detectedObject.base64.length
                    )
                    detectedObject.bitmap =
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes!!.size, BitmapFactory.Options().apply {
                            inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888
                            outWidth = detectedObject.width
                            outHeight = detectedObject.height
                        })
                    decodedBytes = null
                }.also { _detactedObjects.emit(result.toList()) }
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
}