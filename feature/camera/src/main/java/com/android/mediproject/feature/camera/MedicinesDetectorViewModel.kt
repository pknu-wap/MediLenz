package com.android.mediproject.feature.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.ai.AiModel
import com.android.mediproject.core.ai.AiModelManager
import com.android.mediproject.core.ai.camera.CameraController
import com.android.mediproject.core.ai.camera.CameraHelper
import com.android.mediproject.core.ai.model.CapturedDetectionEntity
import com.android.mediproject.core.ai.model.InferenceState
import com.android.mediproject.core.ai.onLoadFailed
import com.android.mediproject.core.ai.onLoaded
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.util.VibrationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.pknujsp.core.annotation.KBindFunc
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val cameraController: CameraController,
    private val vibrationManager: VibrationManager,
    @Named(AiModelManager.Detector) detectorModelManager: AiModel,
) : BaseViewModel() {

    private val aiModelState = detectorModelManager.aiModelState

    private val _cameraConnectionState = MutableStateFlow<CameraConnectionState>(CameraConnectionState.Disconnected)
    val cameraConnectionState get() = _cameraConnectionState.asStateFlow()

    // 검출 정보 기록
    private val _captureState = MutableSharedFlow<InferenceState<CapturedDetectionEntity>>(replay = 1, extraBufferCapacity = 1)
    val captureState get() = _captureState.asSharedFlow()

    private val vibrateDuration = 50L

    fun connectCamera(previewView: PreviewView, viewLifeCycleOwner: LifecycleOwner, detectionCallback: CameraHelper.OnDetectionListener) {
        viewModelScope.launch {
            aiModelState.collect { state ->
                state.onLoaded {
                    if (cameraConnectionState.value is CameraConnectionState.Disconnected) {
                        realConnectCamera(previewView, viewLifeCycleOwner, detectionCallback)
                    }
                }.onLoadFailed { _cameraConnectionState.value = CameraConnectionState.UnableToConnect }
            }
        }
    }

    private suspend fun realConnectCamera(
        previewView: PreviewView,
        viewLifeCycleOwner: LifecycleOwner,
        detectionCallback: CameraHelper.OnDetectionListener,
    ) {
        cameraController.fragmentLifeCycleOwner = viewLifeCycleOwner
        cameraController.detectionCallback = detectionCallback

        cameraController.connectCamera(previewView).onSuccess {
            _cameraConnectionState.value = CameraConnectionState.Connected
        }
    }


    private fun capture(capturedDetectionEntity: CapturedDetectionEntity) {
        viewModelScope.launch {
            vibrationManager.vibrate(vibrateDuration)
            withContext(defaultDispatcher) {
                val result = if (capturedDetectionEntity.items.isEmpty()) {
                    InferenceState.Failure
                } else {
                    capturedDetectionEntity.separateImages()
                    InferenceState.Success(
                        capturedDetectionEntity,
                    )
                }
                _captureState.emit(result)
            }
        }
    }

    var captureFunc: (capturedDetectionEntity: CapturedDetectionEntity) -> Unit = ::capture

    fun disconnectCamera() {
        viewModelScope.launch {
            _cameraConnectionState.value = CameraConnectionState.Disconnected
        }
    }
}

@KBindFunc
sealed interface CameraConnectionState {
    object Connected : CameraConnectionState
    object Disconnected : CameraConnectionState
    object UnableToConnect : CameraConnectionState
}
