package com.android.mediproject.feature.medicine.granule

import android.content.Context
import android.text.Spanned
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetGranuleIdentificationUseCase
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GranuleInfoViewModel @Inject constructor(
    private val getGranuleIdentificationUseCase: GetGranuleIdentificationUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _granuleIdentification = MutableStateFlow<UiState<GranuleIdentificationInfoDto>>(UiState.Loading)
    val granuleIdentification get() = _granuleIdentification.asStateFlow()

    private val _granuleTextTags = MutableStateFlow<Spanned?>(null)
    val granuleTextTags get() = _granuleTextTags.asStateFlow()

    fun getGranuleIdentificationInfo(
        itemName: String?, entpName: String?, itemSeq: String?, context: Context
    ) = viewModelScope.launch {
        _granuleIdentification.value = UiState.Loading
        getGranuleIdentificationUseCase(itemName, entpName, itemSeq).fold(onSuccess = {
            _granuleIdentification.value = UiState.Success(it)

            launch(defaultDispatcher) {
                _granuleTextTags.value = getGranuleIdentificationUseCase.createDataTags(context, it)
            }
        }, onFailure = { _granuleIdentification.value = UiState.Error(it.message ?: "failed") })
    }

}