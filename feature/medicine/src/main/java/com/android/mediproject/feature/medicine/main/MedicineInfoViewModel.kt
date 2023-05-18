package com.android.mediproject.feature.medicine.main

import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.core.text.toSpanned
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val medicineInfoMapper: MedicineInfoMapper,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _medicineDetails = MutableStateFlow<UiState<MedicineDetatilInfoDto>>(UiState.Loading)
    val medicineDetails get() = _medicineDetails.asStateFlow()

    val medicineName = savedStateHandle.getStateFlow("medicineName", "")
    private val _medicinePrimaryInfo = MutableStateFlow<MedicinePrimaryInfoDto?>(null)

    val medicinePrimaryInfo get() = _medicinePrimaryInfo.asStateFlow()


    fun loadMedicineDetails(medicineName: String) {
        viewModelScope.launch {
            savedStateHandle["medicineName"] = medicineName

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


    fun retriveDosage(): Flow<Spanned> = flow {
        viewModelScope.launch(defaultDispatcher) {
            medicineDetails.value.run {
                if (this is UiState.Success) {
                    medicineInfoMapper.toDosageInfo(this.data.udDocData)
                } else {
                    SpannableStringBuilder("").toSpanned()
                }
            }.also {
                emit(it)
            }
        }
    }

    fun retrieveEfficacyEffect(): Flow<Spanned> = flow {
        viewModelScope.launch(defaultDispatcher) {
            medicineDetails.value.run {
                if (this is UiState.Success) {
                    emit(medicineInfoMapper.toEfficacyEffect(this.data.eeDocData))
                } else {
                    emit(SpannableStringBuilder("").toSpanned())
                }
            }
        }
    }


    fun retrieveMedicineInfo(): Flow<Spanned> = flow {
        viewModelScope.launch(defaultDispatcher) {
            medicineDetails.value.run {
                if (this is UiState.Success) {
                    medicineInfoMapper.toMedicineInfo(this.data)
                } else {
                    SpannableStringBuilder("").toSpanned()
                }
            }.also {
                emit(it)
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
 * @property medicineEngName 약 영문 이름
 */
data class MedicinePrimaryInfoDto(
    val medicineName: String, val imgUrl: String, val entpName: String, val itemSequence: String, val medicineEngName: String
)

@Parcelize
enum class BasicInfoType : Parcelable {
    EFFICACY_EFFECT, DOSAGE, MEDICINE_INFO, PRECAUTIONS
}