package com.android.mediproject.core.model.remote.token

import java.time.Duration
import java.time.LocalDateTime

// 리프레시 토큰 만료 시간
val REFRESH_TOKEN_EXPIRES_IN: Duration = Duration.ofDays(180L)

// 액세스 토큰 만료 시간
val ACCESS_TOKEN_EXPIRES_IN: Duration = Duration.ofMinutes(30L)

/**
 * 서버로 부터 응답받은 토큰
 *
 */
data class NewTokensFromServer(
    val accessToken: CharArray,
    val refreshToken: CharArray,
    val requestBehavior: RequestBehavior,
) {

    private var _refreshTokenExpireDateTime: LocalDateTime? = null
    private var _accessTokenExpireDateTime: LocalDateTime? = null

    val refreshTokenExpireDateTime: LocalDateTime
        get() = _refreshTokenExpireDateTime!!

    val accessTokenExpireDateTime: LocalDateTime
        get() = _accessTokenExpireDateTime!!

    init {
        when (requestBehavior) {
            RequestBehavior.NewTokens -> {
                // 토큰 만료 예정 시각을 설정
                val now = LocalDateTime.now()
                _accessTokenExpireDateTime = now.plus(ACCESS_TOKEN_EXPIRES_IN)
                _refreshTokenExpireDateTime = now.plus(REFRESH_TOKEN_EXPIRES_IN)
            }

            RequestBehavior.ReissueTokens -> {
                // 토큰 재발급이므로 액세스 토큰만 저장
                _accessTokenExpireDateTime = LocalDateTime.now().plus(ACCESS_TOKEN_EXPIRES_IN)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewTokensFromServer

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
