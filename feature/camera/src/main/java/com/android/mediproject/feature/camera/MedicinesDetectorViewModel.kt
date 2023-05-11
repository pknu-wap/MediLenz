package com.android.mediproject.feature.camera

import android.content.res.AssetManager
import android.view.SurfaceHolder
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.ai.DetectedObject
import com.android.mediproject.feature.camera.ai.Yolo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val yolo = MutableStateFlow(Yolo()).asStateFlow()
    private val _detactedObjects = MutableStateFlow(emptyArray<DetectedObject>())
    val detectedObjects = _detactedObjects.asStateFlow()

    fun loadModel(assetManager: AssetManager) {
        viewModelScope.launch(ioDispatcher) {
            yolo.value.loadModel(assetManager)
        }
    }

    fun getDetectedObjects() {
        viewModelScope.launch(ioDispatcher) {
            val result = yolo.value.detectedObjects()
            yolo.value.closeCamera()

            if (result == null) {
                _detactedObjects.value = emptyArray()
            } else {
                _detactedObjects.value = result
            }
        }
    }

    fun surfaceCreated(holder: SurfaceHolder) {
        viewModelScope.launch { yolo.value.setOutputWindow(holder.surface) }
    }

    fun openCamera() {
        viewModelScope.launch { yolo.value.openCamera(1) }
    }

    fun closeCamera() {
        viewModelScope.launch { yolo.value.closeCamera() }
    }

    override fun onCleared() {
        super.onCleared()
    }
}