package com.android.mediproject.core.model.user.remote

import com.android.mediproject.core.model.user.UserDto
import kotlinx.serialization.Serializable

/**
 * 유저 정보 데이터
 *
 * @property nickname 유저 이름
 * @property userId 유저 아이디
 * @property email 유저 이메일
 * @property message 응답 메시지
 */
@Serializable
data class UserResponse(
    val nickname: String, val userId: Long, val email: String, val message: String
)

fun UserResponse.toUserDto() = UserDto(nickName = nickname)