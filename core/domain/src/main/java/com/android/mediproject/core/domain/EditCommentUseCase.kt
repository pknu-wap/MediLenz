package com.android.mediproject.core.domain

import com.android.mediproject.core.data.comments.CommentsRepository
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditCommentUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository,
) {

    /**
     * 댓글을 수정합니다.
     */
    fun applyEditedComment(parameter: EditCommentParameter): Flow<Result<Unit>> =
        commentsRepository.editComment(parameter).mapLatest { result ->
            result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
        }

    /**
     * 댓글을 등록합니다.
     */
    fun applyNewComment(parameter: NewCommentParameter): Flow<Result<Unit>> =
        commentsRepository.applyNewComment(parameter).mapLatest { result ->
            result.fold(
                onSuccess = {
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) },
            )
        }

    /**
     * 댓글 삭제
     */
    fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<Unit>> =
        commentsRepository.deleteComment(parameter).mapLatest { result ->
            result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
        }

    /**
     * 댓글 좋아요
     */
    fun likeComment(parameter: LikeCommentParameter): Flow<Result<Unit>> = commentsRepository.likeComment(parameter).mapLatest { result ->
        result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
    }
}
