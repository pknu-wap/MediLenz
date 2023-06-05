package com.android.mediproject.feature.medicine.main

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher) : BaseViewModel() {

    private val _medicinePrimaryInfo = MutableSharedFlow<MedicineInfoArgs>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val medicinePrimaryInfo get() = _medicinePrimaryInfo.asSharedFlow()

    val medicineDetails: StateFlow<UiState<MedicineDetatilInfoDto>> = medicinePrimaryInfo.flatMapLatest { primaryInfo ->
        getMedicineDetailsUseCase(itemName = primaryInfo.itemKorName).map { result ->
            result.fold(onSuccess = { UiState.Success(it) }, onFailure = { UiState.Error(it.message ?: "failed") })
        }
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = UiState.Initial)

    fun setMedicinePrimaryInfo(medicineArgs: MedicineInfoArgs) {
        viewModelScope.launch {
            _medicinePrimaryInfo.emit(medicineArgs)
        }
    }
}