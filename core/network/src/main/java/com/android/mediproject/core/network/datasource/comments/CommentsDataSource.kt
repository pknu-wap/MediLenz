package com.android.mediproject.core.network.datasource.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import kotlinx.coroutines.flow.Flow

interface CommentsDataSource {

    suspend fun getCommentsForAMedicineCatching(itemSeq: String): Result<List<MedicineCommentsResponse>>

    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>>

    fun applyEditedComment(editedCommentDto: EditedCommentDto): Flow<Result<Unit>>

    fun applyNewComment(newCommentDto: NewCommentDto): Flow<Result<Unit>>
    fun deleteComment(commentId: Int): Flow<Result<Unit>>

    fun likeComment(commentId: Int): Flow<Result<Unit>>
}