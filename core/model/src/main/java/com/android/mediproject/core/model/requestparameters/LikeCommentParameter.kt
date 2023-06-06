package com.android.mediproject.core.model.requestparameters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 댓글 좋아요를 위한 파라미터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property userId 사용자 id
 */
@Serializable
data class LikeCommentParameter(
    @SerialName("commentId") val commentId: Long, // 5
    @SerialName("userId") val userId: Long)