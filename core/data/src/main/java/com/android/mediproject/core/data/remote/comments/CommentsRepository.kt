package com.android.mediproject.core.data.remote.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    fun getCommentsForAMedicine(
        itemSeq: String,
    ): Flow<PagingData<MedicineCommentsResponse>>

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>>

    /**
     * 댓글을 수정합니다.
     */
    fun applyEditedComment(editedCommentDto: EditedCommentDto): Flow<Result<Unit>>


    /**
     * 댓글을 등록합니다.
     */
    fun applyNewComment(newCommentDto: NewCommentDto): Flow<Result<Unit>>

    /**
     * 댓글 삭제 클릭
     */
    fun deleteComment(commentId: Int): Flow<Result<Unit>>

    /**
     * 댓글 좋아요 클릭
     */
    fun likeComment(commentId: Int): Flow<Result<Unit>>
}