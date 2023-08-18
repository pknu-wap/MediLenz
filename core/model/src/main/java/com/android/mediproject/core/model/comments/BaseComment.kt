package com.android.mediproject.core.model.comments

import com.android.mediproject.core.annotation.UiModelMapping
import com.android.mediproject.core.model.common.UiModel
import com.android.mediproject.core.model.common.UiModelMapper

data class BaseComment(
    val commentId: Long,
    val userId: Long,
    val userName: String,
    val isReply: Boolean = false,
    val subordinationId: Long = -1L,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
) : UiModel


@UiModelMapping
class BaseCommentUiModelMapper(override val source: Comment) : UiModelMapper<BaseComment>() {

    override fun convert(): BaseComment {
        return source.run {
            BaseComment(
                commentId = commentId,
                userId = userId,
                userName = userName,
                isReply = isReply,
                subordinationId = subordinationId,
                content = content,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

    }

}
