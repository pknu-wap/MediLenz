package com.android.mediproject.feature.camera.confirm

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.ai.AiModel
import com.android.mediproject.core.ai.AiModelManager
import com.android.mediproject.core.ai.classification.MedicineClassifier
import com.android.mediproject.core.ai.model.CapturedDetectionEntity
import com.android.mediproject.core.ai.model.InferenceState
import com.android.mediproject.core.ai.onLoaded
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val medicineClassifier: MedicineClassifier,
    @Named(AiModelManager.Classifier) classifierModelManager: AiModel,
) : BaseViewModel() {

    private val aiModelState = classifierModelManager.aiModelState

    private val _bitmap = MutableStateFlow<UiState<Bitmap>>(UiState.Initial)
    val bitmap get() = _bitmap

    private val _classificationResult = MutableStateFlow<InferenceState<ClassificationResult>>(InferenceState.Initial)
    val classificationResult get() = _classificationResult

    private val capturedDetection = MutableStateFlow<CapturedDetectionEntity?>(null)

    fun createBitmap(capturedDetectionEntity: CapturedDetectionEntity) {
        viewModelScope.launch(defaultDispatcher) {
            capturedDetection.value = capturedDetectionEntity
            _bitmap.value = UiState.Success(ObjectBitmapCreator.createBitmapWithObjects(capturedDetectionEntity))
        }
    }

    fun classify() {
        viewModelScope.launch(defaultDispatcher) {
            classificationResult.value = InferenceState.Inferencing
            aiModelState.collectLatest { model ->
                model.onLoaded {
                    val detection = capturedDetection.value!!
                    val bitmaps = detection.items.map {
                        it.bitmap!!
                    }

                    medicineClassifier.classify(bitmaps).collect { result ->
                        result.onSuccess {
                            val items = it.items.mapIndexed { i, item ->
                                ClassificationResult.Item(
                                    itemSeq = item.itemSeq, score = item.score, bitmap = detection.items[i].bitmap!!,
                                )
                            }
                            delay(2000L)
                            _classificationResult.value = InferenceState.Success(
                                ClassificationResult(
                                    items = items,
                                ),
                            )
                        }.onFailure {
                            _classificationResult.value = InferenceState.Failure
                        }
                    }
                }
            }
        }
    }
}
