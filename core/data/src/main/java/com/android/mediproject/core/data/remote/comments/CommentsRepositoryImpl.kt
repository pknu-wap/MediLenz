package com.android.mediproject.core.data.remote.comments

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.AWS_LOAD_PAGE_SIZE
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsDataSource: CommentsDataSource) : CommentsRepository {
    override fun getCommentsForAMedicine(medicineId: Long): Flow<PagingData<CommentListResponse.Comment>> =
        Pager(config = PagingConfig(pageSize = AWS_LOAD_PAGE_SIZE, prefetchDistance = 5), pagingSourceFactory = {
            CommentsListDataSourceImpl(commentsDataSource, medicineId)
        }).flow

    override fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO("Not yet implemented")
    }

    override fun applyEditedComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>> =
        commentsDataSource.applyEditedComment(parameter)

    override fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>> =
        commentsDataSource.applyNewComment(parameter)

    override fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>> =
        commentsDataSource.deleteComment(parameter)

    override fun likeComment(parameter: LikeCommentParameter): Flow<Result<CommentChangedResponse>> =
        commentsDataSource.likeComment(parameter)

}