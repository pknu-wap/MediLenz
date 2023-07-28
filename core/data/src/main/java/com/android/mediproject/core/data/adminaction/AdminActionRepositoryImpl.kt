package com.android.mediproject.core.data.adminaction

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.adminaction.AdminActionListResponse
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminActionRepositoryImpl @Inject constructor(
    private val adminActionListDataSource: AdminActionListDataSourceImpl,
) : AdminActionRepository {
    override suspend fun getAdminActionList(): Flow<PagingData<AdminActionListResponse.Item>> =
        Pager(
            config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE, prefetchDistance = 5),
            pagingSourceFactory = {
                adminActionListDataSource
            },
        ).flow

}
