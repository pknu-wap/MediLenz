package com.android.mediproject.feature.medicine.precautions.item

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetElderlyCautionUseCase
import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineSafeUsageViewModel @Inject constructor(
    private val getElderlyCautionUseCase: GetElderlyCautionUseCase,

    ) : BaseViewModel() {

    private val _elderlyCaution = MutableStateFlow<UiState<ElderlyCautionDto>>(UiState.Loading)
    val elderlyCaution get() = _elderlyCaution.asStateFlow()


    fun getElderlyCaution(
        itemName: String?, itemSeq: String?
    ) = viewModelScope.launch {
        _elderlyCaution.value = UiState.Loading
        getElderlyCautionUseCase(itemName, itemSeq).fold(onSuccess = {
            _elderlyCaution.value = UiState.Success(it)
        }, onFailure = { _elderlyCaution.value = UiState.Error(it.message ?: "failed") })
    }
}