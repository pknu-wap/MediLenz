package com.android.mediproject.core.network.datasource.news.recallsuspension

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.news.recall.RecallSaleSuspensionListResponse.Item
import javax.inject.Inject

class RecallSaleSuspensionListDataSourceImpl @Inject constructor(
    private val recallSaleSuspensionDataSource: RecallSaleSuspensionDataSource,
) : PagingSource<Int, Item.Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item.Item> {
        val currentPage = params.key ?: 1

        return try {
            recallSaleSuspensionDataSource.getRecallSaleSuspensionList(currentPage).fold(
                onSuccess = { response ->
                    val nextKey = response.body.let { body ->
                        if (body.items.size < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }

                    LoadResult.Page(
                        data = response.body.items.map { item ->
                            item.item
                        }.toList(),
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
