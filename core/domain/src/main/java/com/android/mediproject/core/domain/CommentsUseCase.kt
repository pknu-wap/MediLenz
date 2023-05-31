package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.model.comments.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {

    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     */
    fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<CommentDto>> = commentsRepository.getCommentsForAMedicine(itemSeq).map {
        it.map { response ->
            response.toDto()
        }
    }

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO()
    }


    /**
     * 댓글을 수정합니다.
     */
    fun applyEditedComment(editedCommentDto: EditedCommentDto): Flow<Result<Unit>> = commentsRepository.applyEditedComment(editedCommentDto)


    /**
     * 댓글을 등록합니다.
     */
    fun applyNewComment(newCommentDto: NewCommentDto): Flow<Result<Unit>> = commentsRepository.applyNewComment(newCommentDto)

    /**
     * 댓글 삭제 클릭
     */
    fun deleteComment(commentId: Int): Flow<Result<Unit>> = commentsRepository.deleteComment(commentId)

    /**
     * 댓글 좋아요 클릭
     */
    fun likeComment(commentId: Int): Flow<Result<Unit>> = commentsRepository.likeComment(commentId)
}