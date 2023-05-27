package com.android.mediproject.core.model.user.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 유저 정보 데이터
 *
 * @property nickName 유저 이름
 * @property userId 유저 아이디
 * @property email 유저 이메일
 * @property createdAt 회원가입 날짜
 */
@Serializable
data class UserResponse(
    @SerialName("nickname")
    val nickName: String,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("email")
    val email: String,
    @SerialName("created_at")
    val createdAt: String
)