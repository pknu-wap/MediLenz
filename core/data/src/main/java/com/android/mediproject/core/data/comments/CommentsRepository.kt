package com.android.mediproject.core.data.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    fun getCommentsByMedicineId(
        medicineId: Long,
    ): Flow<PagingData<CommentListResponse.Comment>>

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyCommentsList(): Flow<Result<MyCommentsListResponse>>

    /**
     * 댓글을 수정합니다.
     */
    fun editComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>>

    /**
     * 댓글을 등록합니다.
     */
    fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>>

    /**
     * 댓글 삭제 클릭
     */
    fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>>

    /**
     * 댓글 좋아요 클릭
     */
    fun likeComment(parameter: LikeCommentParameter): Flow<Result<LikeResponse>>
}
