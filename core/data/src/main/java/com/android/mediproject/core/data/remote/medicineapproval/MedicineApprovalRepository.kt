package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.medicineapproval.Item
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetailInfoResponse
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalRepository {
    suspend fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
    ): Flow<PagingData<Item>>

    suspend fun getMedicineDetailInfo(itemName: String): Result<MedicineDetailInfoResponse.Body.Item>
}