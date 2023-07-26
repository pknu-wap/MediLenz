package com.android.mediproject.feature.medicine.precautions.item

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetDurUseCase
import com.android.mediproject.core.domain.GetElderlyCautionUseCase
import com.android.mediproject.core.model.datagokr.durproduct.productlist.DurItemDto
import com.android.mediproject.core.model.datagokr.durproduct.senior.ElderlyCautionDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineSafeUsageViewModel @Inject constructor(
    private val getElderlyCautionUseCase: GetElderlyCautionUseCase,
    private val getDurUseCase: GetDurUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _elderlyCaution = MutableStateFlow<ElderlyCautionDto?>(null)
    val elderlyCaution get() = _elderlyCaution.asStateFlow()


    private val _dur = MutableStateFlow<UiState<DurItemDto>>(UiState.Loading)

    val dur get() = _dur.asStateFlow()


    fun loadDur(
        itemName: String?, itemSeq: String?,
    ) = viewModelScope.launch {
        getDurUseCase.invoke(itemName, itemSeq).map { result ->
            result.fold(
                onSuccess = {
                    _dur.value = UiState.Success(it)
                },
                onFailure = {
                    _dur.value = UiState.Error(it.message ?: "failed")
                },
            )
        }
    }


}
