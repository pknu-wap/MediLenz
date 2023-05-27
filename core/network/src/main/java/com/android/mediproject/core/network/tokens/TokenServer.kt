package com.android.mediproject.core.network.tokens

import java.time.LocalDateTime
import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {
    private var _tokens: TokenServer.Tokens = TokenServer.Tokens(CharArray(0), CharArray(0), LocalDateTime.now(), CharArray(0))
    private val tokens = _tokens

    override fun updateTokens(tokens: TokenServer.Tokens) {
        _tokens = _tokens.copy(accessToken = tokens.accessToken, refreshToken = tokens.refreshToken)
    }

    override fun getAccessToken(): CharArray = tokens.accessToken

    override fun getRefreshToken(): CharArray = tokens.refreshToken

    override fun isTokenEmpty(): Boolean = tokens.isEmpty()

    override fun isExpiredToken(): Boolean = LocalDateTime.now().isAfter(tokens.expirationDateTime)

    override fun getTokens(): TokenServer.Tokens = tokens

    override fun clearTokens() {
        _tokens = TokenServer.Tokens(CharArray(0), CharArray(0), LocalDateTime.now(), CharArray(0))
    }
}

interface TokenServer {

    data class Tokens(
        val accessToken: CharArray, val refreshToken: CharArray, val expirationDateTime: LocalDateTime, val email: CharArray
    ) {

        fun isEmpty(): Boolean = accessToken.isEmpty() && refreshToken.isEmpty()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Tokens

            if (!accessToken.contentEquals(other.accessToken)) return false
            if (!refreshToken.contentEquals(other.refreshToken)) return false
            if (expirationDateTime != other.expirationDateTime) return false
            if (!email.contentEquals(other.email)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = accessToken.contentHashCode()
            result = 31 * result + refreshToken.contentHashCode()
            result = 31 * result + expirationDateTime.hashCode()
            result = 31 * result + email.contentHashCode()
            return result
        }
    }

    /**
     * 토큰을 업데이트한다.
     */
    fun updateTokens(tokens: Tokens)

    /**
     * 토큰을 반환한다.
     */
    fun getTokens(): Tokens

    /**
     * Access Token을 반환한다.
     */
    fun getAccessToken(): CharArray

    /**
     * Refresh Token을 반환한다.
     */
    fun getRefreshToken(): CharArray

    fun isTokenEmpty(): Boolean

    /**
     * 토큰이 만료되었는지 확인한다.
     *
     * @return 만료되었으면 true, 아니면 false
     */
    fun isExpiredToken(): Boolean

    fun clearTokens()
}