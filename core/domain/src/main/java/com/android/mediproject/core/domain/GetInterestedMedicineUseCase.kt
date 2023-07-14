package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.interestedmedicine.InterestedMedicineRepository
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.toInterestedMedicineDto
import com.android.mediproject.core.model.favoritemedicine.toFavoriteMedicineMoreDto
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

    suspend fun getMoreInterestedMedicineList() = channelFlow {
        interestedMedicineRepository.getInterestedMedicineList().map { result ->
            result.fold(onSuccess = { Result.success(it.map { it.toFavoriteMedicineMoreDto() }) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    /**
     * 관심 약 추가 or 해제
     *
     * @param medicineId 약 ID
     * @param like 관심 약 추가 여부
     *
     * like가 true면 관심 약 추가, false면 관심 약 해제 요청
     */
    fun interestedMedicine(medicineId: Long, like: Boolean): Flow<Result<Unit>> = channelFlow {
        if (like) {
            interestedMedicineRepository.addInterestedMedicine(AddInterestedMedicineParameter(medicineId))
                .collect { addInterestedMedicineResponseResult ->
                    val result =
                        addInterestedMedicineResponseResult.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
                    trySend(result)
                }
        } else {
            interestedMedicineRepository.deleteInterestedMedicine(medicineId).collect { deleteInterestedMedicineResponseResult ->
                val result =
                    deleteInterestedMedicineResponseResult.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
                trySend(result)
            }
        }
    }


    /**
     * 관심 약 여부 확인
     */
    fun isInterestedMedicine(medicineId: Long): Flow<Result<CheckFavoriteMedicineResponse>> =
        interestedMedicineRepository.isInterestedMedicine(medicineId)
}