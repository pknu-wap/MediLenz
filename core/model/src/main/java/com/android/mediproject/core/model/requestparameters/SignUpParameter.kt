package com.android.mediproject.core.model.requestparameters

/**
 * 회원가입을 위한 파라미터 클래스입니다.
 *
 * @property email 이메일
 * @property password 비밀번호
 * @property nickName 닉네임
 */
data class SignUpParameter(
    val email: CharArray, val password: CharArray, val nickName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignUpParameter

        if (!email.contentEquals(other.email)) return false
        if (!password.contentEquals(other.password)) return false
        if (nickName != other.nickName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.contentHashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + nickName.hashCode()
        return result
    }

}