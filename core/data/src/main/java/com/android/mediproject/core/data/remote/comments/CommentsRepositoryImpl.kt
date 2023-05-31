package com.android.mediproject.core.data.remote.comments

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.AWS_LOAD_PAGE_SIZE
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsDataSource: CommentsDataSource
) : CommentsRepository {
    override fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<MedicineCommentsResponse>> =
        Pager(config = PagingConfig(pageSize = AWS_LOAD_PAGE_SIZE, prefetchDistance = 5), pagingSourceFactory = {
            CommentsListDataSourceImpl(commentsDataSource, itemSeq)
        }).flow

    override fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO("Not yet implemented")
    }

    override fun applyEditedComment(editedCommentDto: EditedCommentDto): Flow<Result<Unit>> =
        commentsDataSource.applyEditedComment(editedCommentDto)

    override fun applyNewComment(newCommentDto: NewCommentDto): Flow<Result<Unit>> = commentsDataSource.applyNewComment(newCommentDto)

    override fun deleteComment(commentId: Int): Flow<Result<Unit>> = commentsDataSource.deleteComment(commentId)

    override fun likeComment(commentId: Int): Flow<Result<Unit>> = commentsDataSource.likeComment(commentId)


}