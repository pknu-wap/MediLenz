package com.android.mediproject.core.model.requestparameters

/**
 * 로그인을 위한 파라미터 클래스입니다.
 *
 * @property email 이메일
 * @property password 비밀번호
 * @property isSavedEmail 이메일 저장 여부
 */
data class SignInParameter(
    val email: CharArray, val password: CharArray, val isSavedEmail: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignInParameter

        if (!email.contentEquals(other.email)) return false
        if (!password.contentEquals(other.password)) return false
        if (isSavedEmail != other.isSavedEmail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.contentHashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + isSavedEmail.hashCode()
        return result
    }

}