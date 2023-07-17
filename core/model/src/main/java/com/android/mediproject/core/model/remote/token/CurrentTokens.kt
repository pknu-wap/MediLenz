package com.android.mediproject.core.model.remote.token


/**
 * 토큰 정보를 담는 데이터 클래스
 *
 * @property accessToken
 * @property refreshToken
 */
data class CurrentTokens(
    val accessToken: CharArray,
    val refreshToken: CharArray,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrentTokens

        if (!accessToken.contentEquals(other.accessToken)) return false
        if (!refreshToken.contentEquals(other.refreshToken)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accessToken.contentHashCode()
        result = 31 * result + refreshToken.contentHashCode()

        return result
    }
}
