package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.medicineapproval.Item
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalRepository {
    fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
    ): Flow<PagingData<Item>>
}