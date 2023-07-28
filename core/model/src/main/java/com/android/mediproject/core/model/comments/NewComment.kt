package com.android.mediproject.core.model.comments

/**
 * 새로운 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property medicineId 약 id
 * @property userId 회원 id
 * @property content 댓글 내용
 * @property subordinationId 댓글 종속성
 */
data class NewComment(
    val medicineId: String,
    val userId: Int,
    val content: String,
    val subordinationId: Int = -1,
)
