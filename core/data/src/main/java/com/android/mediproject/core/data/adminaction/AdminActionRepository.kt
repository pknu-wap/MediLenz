package com.android.mediproject.core.data.adminaction

import androidx.paging.PagingData
import com.android.mediproject.core.model.adminaction.AdminActionListResponse
import kotlinx.coroutines.flow.Flow

interface AdminActionRepository {
    fun getAdminActionList(
    ): Flow<PagingData<AdminActionListResponse.Item>>

}
