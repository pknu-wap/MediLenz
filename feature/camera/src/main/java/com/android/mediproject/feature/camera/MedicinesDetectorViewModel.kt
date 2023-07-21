package com.android.mediproject.feature.camera

import android.content.Context
import android.graphics.Bitmap
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Size
import androidx.camera.view.PreviewView
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.core.model.ai.DetectionObject
import com.android.mediproject.core.model.ai.DetectionObjects
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import com.android.mediproject.feature.camera.tflite.classification.MedicineClassifionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.gms.vision.detector.Detection
import javax.inject.Inject


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    val aiController: AiController,
    val cameraController: CameraController,
    private val medicineClassifionHelper: MedicineClassifionHelper,
    @ApplicationContext context: Context,
) : BaseViewModel() {

    @Suppress("DEPRECATION")
    private var _vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val vibrator get() = _vibrator!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Loading)
    val aiModelState get() = _aiModelState.asStateFlow()

    // 검출 정보 가록
    private val _inferenceState = MutableSharedFlow<InferenceState>(replay = 1, onBufferOverflow = BufferOverflow.SUSPEND, extraBufferCapacity = 5)
    val inferenceState get() = _inferenceState.asSharedFlow()

    fun startCamera(previewView: PreviewView) {
        viewModelScope.launch(defaultDispatcher) {
            if (aiModelState.value == AiModelState.Loading) {
                cameraController.setupCamera(previewView).onSuccess {
                    _aiModelState.value = AiModelState.Loaded
                }.onFailure {
                    _aiModelState.value = AiModelState.LoadFailed
                }
            }
        }
    }

    private fun capture(detectedObjectResult: DetectedObjectResult) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                100, VibrationEffect.DEFAULT_AMPLITUDE,
            ),
        )
        if (detectedObjectResult.detections.isEmpty()) {
            _inferenceState.tryEmit(InferenceState.DetectFailed)
            return
        }
        cameraController.pause()

        viewModelScope.launch(defaultDispatcher) {
            detectedObjectResult.run {
                backgroundImage.also {
                    // 처리중 오류 발생시 DetectFailed 상태로 변경
                    val scaleFactor =
                        maxOf(realWindowSize.width.toFloat() / resizedWindowSize.height, realWindowSize.height.toFloat() / resizedWindowSize.height)

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
                    _inferenceState.emit(InferenceState.Detected(DetectionObjects(cutted, backgroundImage)))
                }
            }

        }
    }

    var captureFunc: (detectedObjectResult: DetectedObjectResult) -> Unit = ::capture

    override fun onCleared() {
        super.onCleared()
        _inferenceState.resetReplayCache()
        _vibrator = null
    }

    fun classifyMedicine(detectionObjects: DetectionObjects) {
        viewModelScope.launch(defaultDispatcher) {
            _inferenceState.emit(InferenceState.Classified(medicineClassifionHelper.analyze(detectionObjects)))
        }
    }
}

sealed interface AiModelState {
    object Loading : AiModelState
    object Loaded : AiModelState
    object LoadFailed : AiModelState
}

sealed interface InferenceState {
    object Initial : InferenceState
    data class Detected(val detection: DetectionObjects) : InferenceState
    object DetectFailed : InferenceState
    data class Classified(val classificationResult: List<ClassificationResult>) : InferenceState
}

data class DetectedObjectResult(
    val detections: List<Detection>, val backgroundImage: Bitmap, val realWindowSize: Size,
    val resizedWindowSize: Size,
)
