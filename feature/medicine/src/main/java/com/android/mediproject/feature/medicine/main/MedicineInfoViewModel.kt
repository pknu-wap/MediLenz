package com.android.mediproject.feature.medicine.main

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val savedStateHandle: SavedStateHandle,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _medicineDetails = MutableStateFlow<UiState<MedicineDetatilInfoDto>>(UiState.Loading)
    val medicineDetails get() = _medicineDetails.asStateFlow()

    val medicineName = savedStateHandle.getStateFlow("medicineName", "")
    private val _medicinePrimaryInfo = MutableStateFlow<MedicinePrimaryInfoDto?>(null)

    val medicinePrimaryInfo get() = _medicinePrimaryInfo.asStateFlow()


    fun loadMedicineDetails(medicineName: String) {
        viewModelScope.launch {
            getMedicineDetailsUseCase.invoke(medicineName).map { result ->
                result.fold(onSuccess = {
                    _medicineDetails.value = UiState.Success(it)
                }, onFailure = {
                    _medicineDetails.value = UiState.Error(it.message ?: "failed")
                })
            }
        }
    }

    fun setMedicinePrimaryInfo(medicineArgs: MedicineInfoArgs) {
        viewModelScope.launch {
            _medicinePrimaryInfo.value = MedicinePrimaryInfoDto(
                medicineName = medicineArgs.medicineName,
                imgUrl = medicineArgs.imgUrl,
                entpName = medicineArgs.entpName,
                itemSequence = medicineArgs.itemSequence,
                medicineEngName = medicineArgs.medicineEngName
            )
        }
    }


}

/**
 * 약 핵심 정보
 *
 * @property medicineName 약 이름
 * @property imgUrl 약 이미지 URL
 * @property entpName 약 제조사
 * @property itemSequence 약 품목기준코드
 * @property medicineEngName 약 영문 이름
 */
data class MedicinePrimaryInfoDto(
    val medicineName: String, val imgUrl: String, val entpName: String, val itemSequence: String, val medicineEngName: String
)

@Parcelize
enum class BasicInfoType : Parcelable {
    EFFICACY_EFFECT, DOSAGE, MEDICINE_INFO, PRECAUTIONS
}