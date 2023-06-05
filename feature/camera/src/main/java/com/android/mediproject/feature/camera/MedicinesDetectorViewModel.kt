package com.android.mediproject.feature.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.ai.DetectionObjects
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.gms.vision.detector.Detection
import javax.inject.Inject


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val cameraController: CameraController,
    private val aiController: AiController) : BaseViewModel() {

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.NotLoaded)
    val aiModelState get() = _aiModelState.asStateFlow()

    // 검출 정보 가록
    private val _detectionObjects = MutableStateFlow<DetectionState>(DetectionState.Initial)
    val detectionObjects get() = _detectionObjects.asStateFlow()

    fun loadModel(previewView: PreviewView) {
        viewModelScope.launch {
            if (aiModelState.value == AiModelState.NotLoaded) {
                _aiModelState.value = AiModelState.Loading

                withContext(ioDispatcher) {
                    aiController.loadModel()
                    cameraController.setupCamera(previewView)

                }.fold(onSuccess = {
                    _aiModelState.value = AiModelState.Loaded
                }, onFailure = {
                    _aiModelState.value = AiModelState.LoadFailed
                })
            }
        }
    }

    fun capture() {
        viewModelScope.launch {
            _detectionObjects.tryEmit(DetectionState.Detecting)
        }
    }

    fun makeDetectionResult(objects: List<Detection>, width: Int, height: Int) {
        viewModelScope.launch(defaultDispatcher) {
            // 검출된 객체를 기록
            _detectionObjects.value = DetectionState.Detected(DetectionObjects(objects, width, height))
        }
    }

}

sealed class AiModelState {
    object NotLoaded : AiModelState()
    object Loading : AiModelState()
    object Loaded : AiModelState()
    object LoadFailed : AiModelState()
}

sealed class DetectionState {

    object Initial : DetectionState()
    object Detecting : DetectionState()
    data class Detected(val detection: DetectionObjects) : DetectionState()
    object DetectFailed : DetectionState()
}