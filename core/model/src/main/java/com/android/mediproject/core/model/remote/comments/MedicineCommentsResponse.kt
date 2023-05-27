package com.android.mediproject.core.model.remote.comments

/**
 * 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property userId 회원 id
 * @property userName 회원 명
 * @property isReply 댓글 여부(false -> 댓글, true -> 답글)
 * @property subordinationId 댓글 종속성(-1 -> 댓글, 0 이상 -> 대상 유저 id)
 * @property content 댓글 내용
 * @property createdAt 작성 시각
 * @property updatedAt 수정 시각
 */
data class MedicineCommentsResponse(
    val commentId: Int,
    val userId: Int,
    val userName: String,
    val isReply: Boolean,
    val subordinationId: Int,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
)