package com.android.mediproject.core.model.comments


/**
 * 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property medicineName 약 이름
 * @property content 댓글 내용
 * @property createdAt 작성 시각
 */

data class MyCommentDto(
    val commentId: Int,
    val medicineName: String,
    val content: String,
    val createdAt: String,
    val replyCount: Int,
    var onClick: ((MyCommentDto) -> Unit)? = null,
)
