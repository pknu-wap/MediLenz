package com.android.mediproject.core.model.comments

import kotlinx.datetime.LocalDateTime

/**
 * 댓글 정보를 담는 데이터 클래스입니다.
 *
 * @property userId 회원 id
 * @property userName 회원 명
 * @property subordination 댓글 종속성
 * @property content 댓글 내용
 * @property createdAt 작성 시각
 * @property updatedAt 수정 시각
 */
data class CommentDto(
    val userId: Int,
    val userName: String,
    val subordination: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

/*
ID	int	기본키
USER_ID	int	회원 id(외래키)
MEDICINE_ID	varchar2	약 고유코드(외래키)
SUBORDINATION	int	댓글 종속성
CONTENT	varchar2	댓글 내용
CREATED_AT	DATETIME	작성 시각
UPDATED_AT	DATETIME	수정 시각
 */