package com.android.mediproject.core.network.datasource.comments

import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.flow.Flow

interface CommentsDataSource {

    suspend fun getCommentsByMedicineId(medicineId: Long, page: Int, rows: Int, userId: String = "-1"): Result<CommentListResponse>

    suspend fun getMyCommentsList(): Flow<Result<MyCommentsListResponse>>

    fun editComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>>

    fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>>

    fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>>

    fun likeComment(likeCommentParameter: LikeCommentParameter): Flow<Result<LikeResponse>>
}
