package com.android.mediproject.core.model.comments

/**
 * 수정한 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property content 댓글 내용
 */
data class EditedCommentDto(
    val commentId: Int,
    val content: String,
)