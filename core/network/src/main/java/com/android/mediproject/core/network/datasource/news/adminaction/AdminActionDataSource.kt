package com.android.mediproject.core.network.datasource.news.adminaction

import com.android.mediproject.core.model.news.adminaction.AdminActionListResponse

interface AdminActionDataSource {

    suspend fun getAdminActionList(
        pageNo: Int,
    ): Result<AdminActionListResponse>
}
