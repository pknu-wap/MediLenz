package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.remote.medicinedetailinfo.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository
) {

    suspend operator fun invoke(
        itemName: String,
    ): Flow<Result<MedicineDetatilInfoDto>> = medicineApprovalRepository.getMedicineDetailInfo(
        itemName = itemName,
    ).map { result ->
        result.fold(onSuccess = { Result.success(it.toDto()) }, onFailure = { Result.failure(it) })
    }
}