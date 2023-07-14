package com.android.mediproject.core.network.datasource.comments

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.model.comments.CommentListResponse

class CommentsListDataSourceImpl(
    private val commentsDataSource: CommentsDataSource,
    private val medicineId: Long,
) : PagingSource<Int, CommentListResponse.Comment>() {

    override fun getRefreshKey(state: PagingState<Int, CommentListResponse.Comment>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentListResponse.Comment> {
        return try {
            commentsDataSource.getCommentsForAMedicine(medicineId).fold(onSuccess = {
                LoadResult.Page(
                    data = it.commentList,
                    prevKey = null,
                    nextKey = if (it.commentList.size > 1000000) 1 else null,
                )
            }, onFailure = {
                LoadResult.Error(it)
            })
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}