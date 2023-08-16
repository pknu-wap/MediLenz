package com.android.mediproject.feature.camera.confirm

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.ai.tflite.classification.MedicineClassifier
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.core.model.ai.DetectionResultEntity
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.InferenceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val medicineClassifier: MedicineClassifier,
) : BaseViewModel() {

    private val _bitmap = MutableStateFlow<UiState<Bitmap>>(UiState.Initial)
    val bitmap get() = _bitmap

    private val _classificationResult = MutableStateFlow<InferenceState<List<ClassificationResultEntity>>>(InferenceState.Initial)
    val classificationResult get() = _classificationResult

    fun createBitmap(detectionResultEntity: DetectionResultEntity) {
        viewModelScope.launch(defaultDispatcher) {
            _bitmap.value = UiState.Success(ObjectBitmapCreator.createBitmapWithObjects(detectionResultEntity))
        }
    }

    fun classify(detectionResultEntity: DetectionResultEntity) {
        viewModelScope.launch(defaultDispatcher) {
            medicineClassifier.classify(detectionResultEntity).collect { result ->
                result.onSuccess {
                    _classificationResult.value = InferenceState.Success(it)
                }.onFailure {
                    _classificationResult.value = InferenceState.Failure
                }
            }
        }
    }
}
