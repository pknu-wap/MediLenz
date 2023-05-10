package com.android.mediproject.core.network.datasource.penalties

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import com.android.mediproject.core.network.module.PenaltiesNetworkApi
import javax.inject.Inject


class AdminActionDataSourceImpl @Inject constructor(
    private val penaltiesNetworkApi: PenaltiesNetworkApi,
) : PagingSource<Int, AdminActionListResponse.Body.Item>() {

    override fun getRefreshKey(state: PagingState<Int, AdminActionListResponse.Body.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, AdminActionListResponse.Body.Item> {
        val currentPage = params.key ?: 1

        return try {
            val response = penaltiesNetworkApi.getAdminActionList(
                pageNo = currentPage
            )

            if (response.isSuccess()) {
                val nextKey = response.body.let { body ->
                    if (body.items.count() < DATA_GO_KR_PAGE_SIZE) null
                    else currentPage + 1
                }

                PagingSource.LoadResult.Page(
                    data = response.body.items,
                    prevKey = null,
                    nextKey = nextKey,
                )
            } else {
                PagingSource.LoadResult.Error(Throwable(response.header?.resultMsg ?: ""))
            }

        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}