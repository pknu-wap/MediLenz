package com.android.mediproject.core.domain

import androidx.paging.map
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineApprovalListUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository
) {

    operator fun invoke(
        itemName: String?,
        entpName: String?,
    ) = medicineApprovalRepository.getMedicineApprovalList(
        itemName = itemName,
        entpName = entpName,
    ).let { pager ->
        pager.map { pagingData ->
            pagingData.map { item ->
                ApprovedMedicineItemDto(
                    itemSeq = item.itemSeq,
                    itemName = item.itemName,
                    itemEngName = item.itemEngName,
                    entpName = item.entpName,
                    entpEngName = item.entpEngName,
                    entpSeq = item.entpSeq,
                    entpNo = item.entpNo,
                    itemPermitDate = item.itemPermitDate,
                    induty = item.induty,
                    prdlstStdrCode = item.prdlstStdrCode,
                    spcltyPblc = item.spcltyPblc,
                    prductType = item.prductType,
                    prductPrmisnNo = item.prductPrmisnNo,
                    itemIngrName = item.itemIngrName,
                    itemIngrCnt = item.itemIngrCnt,
                    bigPrdtImgUrl = item.bigPrdtImgUrl,
                    permitKindCode = item.permitKindCode,
                    cancelDate = item.cancelDate,
                    cancelName = item.cancelName,
                    ediCode = item.ediCode,
                    bizrno = item.bizrno,
                )
            }
        }
    }
}