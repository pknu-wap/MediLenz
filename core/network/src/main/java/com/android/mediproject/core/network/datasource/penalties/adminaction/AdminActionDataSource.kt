package com.android.mediproject.core.network.datasource.penalties.adminaction

import com.android.mediproject.core.model.adminaction.AdminActionListResponse

interface AdminActionDataSource {

    suspend fun getAdminActionList(
        pageNo: Int,
    ): Result<AdminActionListResponse>
}
