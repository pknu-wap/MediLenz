package com.android.mediproject.core.model.comments

import kotlinx.datetime.LocalDateTime


/**
 * 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property commentId 댓글 id
 * @property userId 회원 id
 * @property userName 회원 명
 * @property isReply 댓글 여부(false -> 댓글, true -> 답글)
 * @property subordination 댓글 종속성(-1 -> 댓글, 0 이상 -> 대상 유저 id)
 * @property content 댓글 내용
 * @property createdAt 작성 시각
 * @property updatedAt 수정 시각
 *
 * @property onClickReply 답글 달기 버튼 클릭 시 실행되는 람다 함수
 * @property onClickDelete 댓글 삭제 버튼 클릭 시 실행되는 람다 함수
 * @property onClickLike 댓글 좋아요 버튼 클릭 시 실행되는 람다 함수
 */

data class MyCommentDto(
    val commentId: Int,
    val userId: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val onClickCommnet: (Int) -> Unit,
)
