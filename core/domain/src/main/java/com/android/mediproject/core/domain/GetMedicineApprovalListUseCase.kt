package com.android.mediproject.core.domain

import androidx.paging.map
import com.android.mediproject.core.data.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.medicine.common.producttype.FilterMedicationProductType
import com.android.mediproject.core.model.medicine.medicineapproval.toApprovedMedicine
import com.android.mediproject.core.model.requestparameters.ApprovalListSearchParameter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineApprovalListUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
) {

    suspend operator fun invoke(
        parameter: ApprovalListSearchParameter,
    ) = medicineApprovalRepository.getMedicineApprovalList(
        itemName = parameter.itemName,
        entpName = parameter.entpName,
        medicationType = when (parameter.medicationProductType) {
            FilterMedicationProductType.ALL -> null
            else -> parameter.medicationProductType.text
        },
    ).map { pagingData ->
        pagingData.map { item ->
            item.toApprovedMedicine()
        }
    }
}
