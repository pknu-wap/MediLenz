package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.remote.medicinedetailinfo.toDto
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository
) {

    suspend operator fun invoke(
        itemName: String,
    ) = medicineApprovalRepository.getMedicineDetailInfo(
        itemName = itemName,
    ).map {
        it.toDto()
    }
}