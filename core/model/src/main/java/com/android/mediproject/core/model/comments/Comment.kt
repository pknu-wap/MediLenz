package com.android.mediproject.core.model.comments

import com.android.mediproject.core.model.common.UiModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


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
 *
 * @property onClickReply 답글 달기 버튼 클릭 시 실행되는 람다 함수
 * @property onClickLike 댓글 좋아요 버튼 클릭 시 실행되는 람다 함수
 * @property onClickApplyEdited 답글 수정 버튼 클릭 시 실행되는 람다 함수
 * @property onClickDelete 답글 삭제 버튼 클릭 시 실행되는 람다 함수
 * @property onClickEdit 답글 수정 버튼 클릭 시 실행되는 람다 함수
 * @property isMine 내가 쓴 댓글인지 여부
 */
data class Comment(
    val commentId: Long,
    val userId: Long,
    val userName: String,
    val isReply: Boolean = false,
    val subordinationId: Long = -1,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    var onClickReply: ((BaseComment) -> Unit)?,
    var onClickLike: ((Long, Boolean) -> Unit)?,
    var onClickDelete: ((Long) -> Unit)?,
    var onClickEdit: ((Comment, Int) -> Unit)?,
    var onClickApplyEdited: ((Comment) -> Unit)?,
    var isMine: Boolean = false,
) : UiModel {

    var isEditing: Boolean = false
    var isLiked: Boolean = false
}

fun CommentListResponse.Comment.toComment() = Comment(
    commentId = id,
    userId = userId,
    userName = nickName,
    isReply = false,
    subordinationId = subordination,
    content = content,
    createdAt = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_ZONED_DATE_TIME).plusHours(10L).format(dateTimeFormatter),
    updatedAt = ZonedDateTime.parse(updatedAt, DateTimeFormatter.ISO_ZONED_DATE_TIME).plusHours(10L).format(dateTimeFormatter),
    onClickReply = null,
    onClickLike = null,
    onClickDelete = null,
    onClickEdit = null,
    onClickApplyEdited = null,
)

fun CommentListResponse.Comment.Reply.toComment() = Comment(
    commentId = id,
    userId = userId,
    userName = nickName,
    isReply = true,
    subordinationId = subordination,
    content = content,
    createdAt = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_ZONED_DATE_TIME).plusHours(10L).format(dateTimeFormatter),
    updatedAt = ZonedDateTime.parse(updatedAt, DateTimeFormatter.ISO_ZONED_DATE_TIME).plusHours(10L).format(dateTimeFormatter),
    onClickReply = null,
    onClickLike = null,
    onClickDelete = null,
    onClickEdit = null,
    onClickApplyEdited = null,
)

private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
