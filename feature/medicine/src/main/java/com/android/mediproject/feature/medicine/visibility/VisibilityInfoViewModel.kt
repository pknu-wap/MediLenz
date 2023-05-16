package com.android.mediproject.feature.medicine.visibility

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetGranuleIdentificationUseCase
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisibilityInfoViewModel @Inject constructor(
    private val getGranuleIdentificationUseCase: GetGranuleIdentificationUseCase,
) : BaseViewModel() {
    
    private val _granuleIdentification = MutableStateFlow<UiState<GranuleIdentificationInfoDto>>(UiState.Loading)
    val granuleIdentification get() = _granuleIdentification.asStateFlow()

    fun getGranuleIdentificationInfo(
        itemName: String?, entpName: String?, itemSeq: String?
    ) = viewModelScope.launch {
        _granuleIdentification.value = UiState.Loading
        getGranuleIdentificationUseCase(itemName, entpName, itemSeq).fold(onSuccess = {
            _granuleIdentification.value = UiState.Success(it)
        }, onFailure = { _granuleIdentification.value = UiState.Error(it.message ?: "failed") })
    }


}