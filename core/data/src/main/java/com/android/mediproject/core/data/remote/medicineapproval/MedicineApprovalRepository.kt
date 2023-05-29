package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.PagingData
import com.android.mediproject.core.model.medicine.medicineapproval.Item
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalRepository {
    suspend fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
    ): Flow<PagingData<Item>>

    suspend fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse.Body.Item>>
}