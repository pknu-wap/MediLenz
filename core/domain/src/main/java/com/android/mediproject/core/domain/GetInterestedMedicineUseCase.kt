package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.interestedmedicine.InterestedMedicineRepository
import com.android.mediproject.core.model.medicine.InterestedMedicine.AddInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.toInterestedMedicineDto
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInterestedMedicineUseCase @Inject constructor(private val interestedMedicineRepository: InterestedMedicineRepository) {

    suspend fun getInterestedMedicineList() = channelFlow {
        interestedMedicineRepository.getInterestedMedicineList().map { result ->
            result.fold(onSuccess = { Result.success(it.map { it.toInterestedMedicineDto() }) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    /**
     * 관심 약 추가
     */
    fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<AddInterestedMedicineResponse>> =
        interestedMedicineRepository.addInterestedMedicine(addInterestedMedicineParameter)

    /**
     * 관심 약 삭제
     */
    fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>> =
        interestedMedicineRepository.deleteInterestedMedicine(medicineId)

    /**
     * 관심 약 여부 확인
     */
    fun isInterestedMedicine(medicineId: Long): Flow<Result<IsInterestedMedicineResponse>> =
        interestedMedicineRepository.isInterestedMedicine(medicineId)
}