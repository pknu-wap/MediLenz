package com.android.mediproject.core.model.requestparameters


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 댓글 작성을 위한 파라미터 클래스입니다.
 *
 * @property content 댓글 내용
 * @property medicineId 약품 id
 * @property userId 유저 id
 */
@Serializable
data class NewCommentParameter(
    @SerialName("content") val content: String, // 테스트 - 서브 댓글 2 - 2
    @SerialName("medicineId") val medicineId: String, // 41
    @SerialName("subOrdinationId") val subOrdinationId: String, // 3
    @SerialName("userId") val userId: String
)