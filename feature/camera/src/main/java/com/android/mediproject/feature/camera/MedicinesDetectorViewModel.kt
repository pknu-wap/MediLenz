package com.android.mediproject.feature.camera

import android.graphics.Bitmap
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.gms.vision.detector.Detection
import javax.inject.Inject
import kotlin.math.max


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    val aiController: AiController,
    val cameraController: CameraController,
    val medicineClassifionHelper: MedicineClassifionHelper
) : BaseViewModel() {


    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.NotLoaded)
    val aiModelState get() = _aiModelState.asStateFlow()

    // 검출 정보 가록
    private val _inferenceState =
        MutableSharedFlow<InferenceState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 5)
    val inferenceState get() = _inferenceState.asSharedFlow()

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
        viewModelScope.launch(defaultDispatcher) {
            _inferenceState.emit(InferenceState.Detecting)
        }
    }

    fun makeDetectionResult(
        objects: List<Detection>, realWindowSize: Size, resizedWindowSize: Size, backgroundImage: Bitmap?
    ) {
        viewModelScope.launch(defaultDispatcher) {
            // 처리중 오류 발생시 DetectFailed 상태로 변경
            val scaleFactor =
                max(realWindowSize.width * 1f / resizedWindowSize.height, realWindowSize.height * 1f / resizedWindowSize.height)
            backgroundImage?.also {
                // 검출된 객체 자르기
                val cutted = objects.map {
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
            } ?: _inferenceState.emit(InferenceState.DetectFailed)
        }
    }

    override fun onCleared() {
        _inferenceState.resetReplayCache()
        super.onCleared()
    }

    fun classifyMedicine(detectionObjects: DetectionObjects) {
        viewModelScope.launch(defaultDispatcher) {
            val classified = medicineClassifionHelper.analyze(detectionObjects)
            _inferenceState.emit(InferenceState.Classified(classified))
        }
    }
}

sealed class AiModelState {
    object NotLoaded : AiModelState()
    object Loading : AiModelState()
    object Loaded : AiModelState()
    object LoadFailed : AiModelState()
}

sealed class InferenceState {
    object Initial : InferenceState()
    object Detecting : InferenceState()
    data class Detected(val detection: DetectionObjects) : InferenceState()
    object DetectFailed : InferenceState()

    data class Classified(val classificationResult: List<ClassificationResult>) : InferenceState()
}