package com.android.mediproject.core.model.requestparameters


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 댓글 삭제를 위한 파라미터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property medicineId 약품 id
 */
@Serializable
data class DeleteCommentParameter(
    @SerialName("commentId") val commentId: Int, // 1
    @SerialName("medicineId") val medicineId: Int // 41
)