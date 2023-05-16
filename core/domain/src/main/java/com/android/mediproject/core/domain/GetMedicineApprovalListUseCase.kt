package com.android.mediproject.core.domain

import androidx.paging.map
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.remote.medicineapproval.toDto
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineApprovalListUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository
) {

    suspend operator fun invoke(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
    ) = medicineApprovalRepository.getMedicineApprovalList(
        itemName = itemName,
        entpName = entpName,
        medicationType = medicationType,
    ).let { pager ->
        pager.map { pagingData ->
            pagingData.map { item ->
                item.toDto()
            }
        }
    }
}