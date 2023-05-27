package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {


    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     */
    suspend fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<CommentDto>> = commentsRepository.getCommentsForAMedicine(itemSeq)
        .let {
            it.map {
                it.map { response ->
                    CommentDto(
                        commentId = response.commentId,
                        userId = response.userId,
                        userName = response.userName,
                        isReply = response.isReply,
                        subordinationId = response.subordinationId,
                        content = response.content,
                        createdAt = response.createdAt.toLocalDateTime(),
                        updatedAt = response.updatedAt.toLocalDateTime(),
                        onClickReply = null,
                        onClickLike = null,
                        onClickDelete = null,
                        onClickEdit = null,
                        onClickApplyEdited = null,
                        onClickEditCancel = null
                    )
                }
            }
        }

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO()
    }

}