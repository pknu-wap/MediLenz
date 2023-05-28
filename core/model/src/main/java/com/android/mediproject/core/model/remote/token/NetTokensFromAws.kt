package com.android.mediproject.core.model.remote.token

/**
 * 서버로 부터 응답으로 받은 토큰 정보
 */
data class NewTokensFromAws(
    val accessToken: CharArray,
    val refreshToken: CharArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewTokensFromAws

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