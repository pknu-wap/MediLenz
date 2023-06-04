package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.interestedmedicine.InterestedMedicineRepository
import com.android.mediproject.core.model.medicine.InterestedMedicine.toInterestedMedicineDto
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInterestedMedicineUseCase @Inject constructor(private val interestedMedicineRepository: InterestedMedicineRepository) {

    suspend fun getInterestedMedicineList() = channelFlow {
        interestedMedicineRepository.getInterestedMedicineList().map { result ->
            result.fold(
                onSuccess = { Result.success(it.map { it.toInterestedMedicineDto() }) },
                onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }
}