package com.android.mediproject.core.data.comments

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.SERVER_PAGE_SIZE
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsDataSource: CommentsDataSource,
) : CommentsRepository {
    override fun getCommentsByMedicineId(medicineId: Long): Flow<PagingData<CommentListResponse.Comment>> = Pager(
        config = PagingConfig(pageSize = SERVER_PAGE_SIZE, prefetchDistance = 0),
        pagingSourceFactory = {
            CommentsListDataSourceImpl(commentsDataSource, medicineId)
        },
    ).flow

    override fun getMyCommentsList(): Result<MyCommentsListResponse> {
        TODO("Not yet implemented")
    }

    override fun applyNewComment(parameter: NewCommentParameter): Result<CommentChangedResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteComment(parameter: DeleteCommentParameter): Result<CommentChangedResponse> {
        TODO("Not yet implemented")
    }

    override fun editComment(parameter: EditCommentParameter): Result<CommentChangedResponse> {
        TODO("Not yet implemented")
    }

    override fun likeComment(parameter: LikeCommentParameter): Result<LikeResponse> {
        TODO("Not yet implemented")
    }
}
