package com.android.mediproject.core.network.datasource.penalties.adminaction

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.news.adminaction.AdminActionListResponse
import javax.inject.Inject


class AdminActionListDataSourceImpl @Inject constructor(
    private val adminActionDataSource: AdminActionDataSource,
) : PagingSource<Int, AdminActionListResponse.Item>() {

    override fun getRefreshKey(state: PagingState<Int, AdminActionListResponse.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AdminActionListResponse.Item> {
        val currentPage = params.key ?: 1

        return try {
            adminActionDataSource.getAdminActionList(currentPage).fold(
                onSuccess = { response ->
                    val nextKey = response.body.let { body ->
                        if (body.items.size < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }
                    LoadResult.Page(
                        data = response.body.items,
                        prevKey = null,
                        nextKey = nextKey,
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                },
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
