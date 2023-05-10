package com.android.mediproject.core.data.remote.adminaction

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import kotlinx.coroutines.flow.Flow

interface AdminActionRepository {
    suspend fun getAdminActionList(
    ): Flow<PagingData<AdminActionListResponse.Body.Item>>

}