package com.android.mediproject.core.datastore

import java.time.LocalDateTime
import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {
    private var _tokens: TokenServer.Tokens = TokenServer.Tokens(CharArray(0), CharArray(0), LocalDateTime.now(), LocalDateTime.now())
    override var tokens
        get() = _tokens
        set(value) {
            _tokens = value
        }


    override fun isTokenEmpty(): Boolean = tokens.isEmpty()

    override fun isExpiredAccessToken(): Boolean = LocalDateTime.now().isAfter(tokens.expirationTimeOfAccessToken)

    override fun isExpiredRefreshToken(): Boolean = LocalDateTime.now().isAfter(tokens.expirationTimeOfRefreshToken)

    override fun clearTokens() {
        _tokens = TokenServer.Tokens(CharArray(0), CharArray(0), LocalDateTime.now(), LocalDateTime.now())
    }
}

interface TokenServer {

    data class Tokens(
        val accessToken: CharArray,
        val refreshToken: CharArray,
        val expirationTimeOfAccessToken: LocalDateTime,
        val expirationTimeOfRefreshToken: LocalDateTime
    ) {

        fun isEmpty(): Boolean = accessToken.isEmpty() && refreshToken.isEmpty()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Tokens

            if (!accessToken.contentEquals(other.accessToken)) return false
            if (!refreshToken.contentEquals(other.refreshToken)) return false
            if (expirationTimeOfAccessToken != other.expirationTimeOfAccessToken) return false
            if (expirationTimeOfRefreshToken != other.expirationTimeOfRefreshToken) return false

            return true
        }

        override fun hashCode(): Int {
            var result = accessToken.contentHashCode()
            result = 31 * result + refreshToken.contentHashCode()
            result = 31 * result + expirationTimeOfAccessToken.hashCode()
            result = 31 * result + expirationTimeOfRefreshToken.hashCode()

            return result
        }

    }

    var tokens: Tokens

    fun isTokenEmpty(): Boolean

    /**
     * 접근 토큰이 만료되었는지 확인한다.
     *
     * @return 만료되었으면 true, 아니면 false
     */
    fun isExpiredAccessToken(): Boolean

    /**
     * 갱신 토큰이 만료되었는지 확인한다.
     *
     * @return 만료되었으면 true, 아니면 false
     */
    fun isExpiredRefreshToken(): Boolean

    fun clearTokens()
}