package com.android.mediproject.core.network.datasource.penalties.adminaction

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import javax.inject.Inject


class AdminActionListDataSourceImpl @Inject constructor(
    private val adminActionDataSource: AdminActionDataSource,
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
            adminActionDataSource.getAdminActionList(currentPage).let { result ->
                result.fold(onSuccess = { response ->
                    val nextKey = response.body?.let { body ->
                        if (body.items.count() < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }
                    LoadResult.Page(
                        data = response.body?.items ?: emptyList(),
                        prevKey = null,
                        nextKey = nextKey,
                    )
                }, onFailure = { throwable ->
                    PagingSource.LoadResult.Error(throwable)
                })
            }

        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}