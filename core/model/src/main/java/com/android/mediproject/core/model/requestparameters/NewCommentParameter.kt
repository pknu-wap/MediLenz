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
    @SerialName("content") val content: String,
    @SerialName("medicine_id") val medicineId: String,
    @SerialName("parent_id") val parentId: String = ROOT_COMMENT_ID,
    @SerialName("user_id") val userId: String,
) {
    companion object {
        const val ROOT_COMMENT_ID = "0"
    }
}
