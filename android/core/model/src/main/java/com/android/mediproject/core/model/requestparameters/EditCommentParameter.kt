package com.android.mediproject.core.model.requestparameters


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 댓글 수정을 위한 파라미터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property content 댓글 내용
 * @property medicineId 약품 id
 */
@Serializable
data class EditCommentParameter(
    @SerialName("commentId") val commentId: Long, // 5
    @SerialName("content") val content: String, // 테스트 - 서브 댓글 1 - 1 - 수정
    @SerialName("medicineId") val medicineId: Long // 41
)