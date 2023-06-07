package com.android.mediproject.core.network.datasource.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.flow.Flow

interface CommentsDataSource {

    suspend fun getCommentsForAMedicine(medicineId: Long): Result<CommentListResponse>

    fun getMyComments(userId: Int): Flow<PagingData<CommentListResponse>>

    fun applyEditedComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>>

    fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>>
    fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>>

    fun likeComment(likeCommentParameter: LikeCommentParameter): Flow<Result<CommentChangedResponse>>
}