package com.android.mediproject.feature.camera

import android.content.Context
import android.graphics.Bitmap
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Size
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.ai.DetectionObject
import com.android.mediproject.core.model.ai.DetectionObjects
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import com.android.mediproject.feature.camera.tflite.CameraHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.pknujsp.core.annotation.KBindFunc
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.gms.vision.detector.Detection
import javax.inject.Inject


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val aiController: AiController,
    private val cameraController: CameraController,
    @ApplicationContext context: Context,
) : BaseViewModel() {

    @Suppress("DEPRECATION")
    private var vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    private val aiModelState get() = _aiModelState.asStateFlow()

    private val _cameraConnectionState = MutableStateFlow<CameraConnectionState>(CameraConnectionState.Disconnected)
    val cameraConnectionState get() = _cameraConnectionState.asStateFlow()

    // 검출 정보 가록
    private val _inferenceState = MutableSharedFlow<InferenceState>(replay = 1, extraBufferCapacity = 1)
    val inferenceState get() = _inferenceState.asSharedFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            _aiModelState.value = AiModelState.Loading

            aiController.getObjectDetectorStatus().onSuccess {
                _aiModelState.value = AiModelState.Loaded
            }.onFailure {
                _aiModelState.value = AiModelState.LoadFailed
            }
        }
    }

    fun connectCamera(previewView: PreviewView, viewLifeCycleOwner: LifecycleOwner, detectionCallback: CameraHelper.ObjDetectionCallback) {
        viewModelScope.launch {
            aiModelState.value.onLoaded {
                if (cameraConnectionState.value is CameraConnectionState.Disconnected) {
                    realConnectCamera(previewView, viewLifeCycleOwner, detectionCallback)
                }
            }.onLoading {
                aiModelState.collect { aiState ->
                    aiState.onLoaded {
                        realConnectCamera(previewView, viewLifeCycleOwner, detectionCallback)
                    }.onLoadFailed { _cameraConnectionState.value = CameraConnectionState.UnableToConnect }
                }
            }.onLoadFailed { _cameraConnectionState.value = CameraConnectionState.UnableToConnect }
        }
    }

    private suspend fun realConnectCamera(
        previewView: PreviewView,
        viewLifeCycleOwner: LifecycleOwner,
        detectionCallback: CameraHelper.ObjDetectionCallback,
    ) {
        cameraController.fragmentLifeCycleOwner = viewLifeCycleOwner
        cameraController.detectionCallback = detectionCallback

        cameraController.connectCamera(previewView).onSuccess {
            _cameraConnectionState.value = CameraConnectionState.Connected
        }
    }


    private fun capture(detectedObjectResult: DetectedObjectResult) {
        viewModelScope.launch {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    100, VibrationEffect.DEFAULT_AMPLITUDE,
                ),
            )
            if (detectedObjectResult.detections.isEmpty()) {
                _inferenceState.emit(InferenceState.DetectFailed)
                return@launch
            }

            withContext(defaultDispatcher) {
                detectedObjectResult.run {
                    // 처리중 오류 발생시 DetectFailed 상태로 변경
                    val scaleFactor =
                        maxOf(
                            realWindowSize.width.toFloat() / resizedWindowSize.height,
                            realWindowSize.height.toFloat() / resizedWindowSize.height,
                        )

                    // 검출된 객체 자르기
                    val cutted = detections.map {
                        val boundingBox = it.boundingBox

                        val top = boundingBox.top * scaleFactor
                        val bottom = boundingBox.bottom * scaleFactor
                        val left = boundingBox.left * scaleFactor
                        val right = boundingBox.right * scaleFactor

                        val width = right - left
                        val height = bottom - top

                        val croppedBitmap = Bitmap.createBitmap(backgroundImage, left.toInt(), top.toInt(), width.toInt(), height.toInt())
                        DetectionObject(it, croppedBitmap)
                    }
                    _inferenceState.emit(
                        InferenceState.Detected(
                            DetectionObjects(
                                cutted, backgroundImage, resizedWindowSize.width,
                                resizedWindowSize
                                    .height,
                            ),
                        ),
                    )
                }
            }

        }
    }

    var captureFunc: (detectedObjectResult: DetectedObjectResult) -> Unit = ::capture

    override fun onCleared() {
        super.onCleared()
        vibrator = null
    }

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

@KBindFunc
sealed interface AiModelState {
    object Initial : AiModelState
    object Loading : AiModelState
    object Loaded : AiModelState
    object LoadFailed : AiModelState
}

@KBindFunc
sealed interface InferenceState {
    object Initial : InferenceState
    data class Detected(val detection: DetectionObjects, var consumed: Boolean = false) : InferenceState
    object DetectFailed : InferenceState
}

data class DetectedObjectResult(
    val detections: List<Detection>, val backgroundImage: Bitmap, val realWindowSize: Size,
    val resizedWindowSize: Size,
)
