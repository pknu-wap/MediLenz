package com.android.mediproject.feature.medicine.main

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase, private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _medicineDetails = MutableStateFlow<UiState<MedicineDetatilInfoDto>>(UiState.Initial)
    val medicineDetails get() = _medicineDetails.asStateFlow()

    val medicineName = savedStateHandle.getStateFlow("medicineName", "")
    private val _medicinePrimaryInfo = MutableStateFlow<MedicinePrimaryInfoDto?>(null)

    val medicinePrimaryInfo get() = _medicinePrimaryInfo.asStateFlow()


    fun loadMedicineDetails(medicineName: String) {
        viewModelScope.launch {
            savedStateHandle["medicineName"] = medicineName

            _medicineDetails.value = UiState.Loading
            getMedicineDetailsUseCase.invoke(medicineName).also { result ->
                result.fold(onSuccess = { medicineDetails ->
                    _medicineDetails.value = UiState.Success(medicineDetails)
                }, onFailure = { throwable ->
                    _medicineDetails.value = UiState.Error(throwable.message ?: "오류가 발생했습니다.")
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


    fun getDetailInfo(type: BasicInfoType): Any {
        medicineDetails.value.let {
            (it as UiState.Success).let { success ->
                return when (type) {
                    BasicInfoType.EFFICACY_EFFECT -> success.data.eeDocData
                    BasicInfoType.DOSAGE -> success.data.udDocData
                    BasicInfoType.MEDICINE_INFO -> success.data.ingredientName
                }
            }
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
 */
data class MedicinePrimaryInfoDto(
    val medicineName: String, val imgUrl: String, val entpName: String, val itemSequence: String, val medicineEngName: String
)

@Parcelize
enum class BasicInfoType : Parcelable {
    EFFICACY_EFFECT, DOSAGE, MEDICINE_INFO
}