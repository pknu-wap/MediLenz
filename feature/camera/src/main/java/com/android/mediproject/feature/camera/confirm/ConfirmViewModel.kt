package com.android.mediproject.feature.camera.confirm

import android.graphics.Bitmap
import android.util.Size
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.ai.DetectionObjects
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.util.ObjectBitmapCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _bitmap = MutableStateFlow<UiState<Bitmap>>(UiState.Initial)
    val bitmap get() = _bitmap

    fun createBitmap(detectionObjects: DetectionObjects) {
        viewModelScope.launch(defaultDispatcher) {
            _bitmap.value = UiState.Success(ObjectBitmapCreator.createBitmapWithObjects(detectionObjects))
        }
    }

}
