package com.android.mediproject.core.model.requestparameters

import kotlinx.serialization.Serializable

/**
 * 댓글 좋아요를 위한 파라미터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property medicineId 약 id
 * @property toLike 좋아요를 누를지 취소할지 여부
 */
@Serializable
data class LikeCommentParameter(
    val commentId: Long, // 5
    val medicineId: Long, val toLike: Boolean
)