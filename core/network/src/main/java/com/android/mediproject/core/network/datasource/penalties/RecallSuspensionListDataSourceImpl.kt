package com.android.mediproject.core.network.datasource.penalties

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse.Body.Item.Item
import javax.inject.Inject

class RecallSuspensionListDataSourceImpl @Inject constructor(
    private val recallSuspensionDataSource: RecallSuspensionDataSource,
) : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Item> {
        val currentPage = params.key ?: 1

        return try {
            val response = recallSuspensionDataSource.getRecallSuspensionList(currentPage)

            response.header?.let { header ->
                if (header.resultCode == "00") {
                    val nextKey = response.body?.let { body ->
                        if (body.items.count() < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }

                    PagingSource.LoadResult.Page(
                        data = response.body?.items?.map { item ->
                            item.item
                        } ?: emptyList(),
                        prevKey = null,
                        nextKey = nextKey,
                    )
                } else {
                    PagingSource.LoadResult.Error(Throwable(header.resultMsg))
                }
            } ?: kotlin.run {
                PagingSource.LoadResult.Error(Throwable("header is null"))
            }

        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}