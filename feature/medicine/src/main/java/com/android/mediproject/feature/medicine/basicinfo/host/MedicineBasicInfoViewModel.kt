package com.android.mediproject.feature.medicine.basicinfo.host

import android.text.SpannableStringBuilder
import androidx.core.text.toSpanned
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineBasicInfoViewModel @Inject constructor(
    private val medicineInfoMapper: MedicineInfoMapper,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher

) : BaseViewModel() {


    private val _medicineDetails = MutableStateFlow<UiState<MedicineDetatilInfoDto>>(UiState.Loading)
    private val medicineDetails get() = _medicineDetails.asStateFlow()


    fun setMedicineDetails(uiState: UiState<MedicineDetatilInfoDto>) {
        viewModelScope.launch {
            _medicineDetails.value = uiState
        }
    }

    val efficacyEffect by lazy {
        medicineDetails.filter { it is UiState.Success }.map {
            medicineInfoMapper.toEfficacyEffect((it as UiState.Success).data.eeDocData)
        }.flowOn(defaultDispatcher).catch {
            emit(SpannableStringBuilder("").toSpanned())
        }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = SpannableStringBuilder("").toSpanned())
    }

    val dosage by lazy {
        medicineDetails.filter { it is UiState.Success }.map {
            medicineInfoMapper.toDosageInfo((it as UiState.Success).data.udDocData)
        }.flowOn(defaultDispatcher).catch {
            emit(SpannableStringBuilder("").toSpanned())
        }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = SpannableStringBuilder("").toSpanned())
    }

    val medicineInfo by lazy {
        medicineDetails.filter { it is UiState.Success }.map {
            medicineInfoMapper.toMedicineInfo((it as UiState.Success).data)
        }.flowOn(defaultDispatcher).catch {
            emit(SpannableStringBuilder("").toSpanned())
        }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = SpannableStringBuilder("").toSpanned())
    }
}