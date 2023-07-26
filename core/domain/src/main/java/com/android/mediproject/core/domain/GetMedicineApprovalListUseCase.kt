package com.android.mediproject.core.domain

import androidx.paging.map
import com.android.mediproject.core.data.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.constants.MedicationType
import com.android.mediproject.core.model.medicine.medicineapproval.toApprovedMedicine
import com.android.mediproject.core.model.requestparameters.ApprovalListSearchParameter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineApprovalListUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
) {

    /**
     * 약품 목록 조회 UseCase
     */
    suspend operator fun invoke(
        parameter: ApprovalListSearchParameter,
    ) = medicineApprovalRepository.getMedicineApprovalList(
        itemName = parameter.itemName,
        entpName = parameter.entpName,
        medicationType = when (parameter.medicationType) {
            MedicationType.ALL -> null
            else -> parameter.medicationType.description
        },
    ).map { pagingData ->
        pagingData.map { item ->
            item.toApprovedMedicine()
        }
    }
}
