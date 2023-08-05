package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.PagingData
import com.android.mediproject.core.model.medicine.medicineapproval.Item
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalRepository {
    fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
    ): Flow<PagingData<Item>>

    fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse.Body.Item>>
    fun getMedicineDetailInfoByItemSeq(itemSeqs: List<String>): Flow<Result<List<MedicineDetailInfoResponse.Body.Item>>>
}