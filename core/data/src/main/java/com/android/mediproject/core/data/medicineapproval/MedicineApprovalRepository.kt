package com.android.mediproject.core.data.medicineapproval

import androidx.paging.PagingData
import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalRepository {
    suspend fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
    ): Flow<PagingData<MedicineApprovalListResponse.Item>>

    fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse.Item>>
    fun getMedicineDetailInfoByItemSeq(itemSeqs: List<String>): Flow<Result<List<MedicineDetailInfoResponse.Item>>>
}
