package com.android.mediproject.core.domain

import com.android.mediproject.core.data.comments.CommentsRepository
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditCommentUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository,
) {


    fun applyEditedComment(parameter: EditCommentParameter): Result<Unit> = commentsRepository.editComment(parameter).let { result ->
        result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
    }


    fun applyNewComment(parameter: NewCommentParameter): Result<Unit> = commentsRepository.applyNewComment(parameter).fold(
        onSuccess = {
            Result.success(Unit)
        },
        onFailure = { Result.failure(it) },
    )


    fun deleteComment(parameter: DeleteCommentParameter): Result<Unit> =
        commentsRepository.deleteComment(parameter).fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })


    fun likeComment(parameter: LikeCommentParameter): Result<Unit> =
        commentsRepository.likeComment(parameter).fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })

}
