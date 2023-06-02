package com.android.mediproject.core.network.datasource.comments

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.AWS_LOAD_PAGE_SIZE
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse

class CommentsListDataSourceImpl(
    private val commentsDataSource: CommentsDataSource,
    private val itemSeq: String,
) : PagingSource<Int, MedicineCommentsResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MedicineCommentsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, MedicineCommentsResponse> {
        val currentPage = params.key ?: 1

        return try {
            commentsDataSource.getCommentsForAMedicineCatching(
                itemSeq
            ).fold(onSuccess = {
                val nextKey = it.let { body ->
                    if (body.size <= AWS_LOAD_PAGE_SIZE) null
                    else currentPage + 1
                }

                PagingSource.LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = nextKey,
                )
            }, onFailure = { PagingSource.LoadResult.Error(it) })

        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}