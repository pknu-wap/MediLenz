package com.android.mediproject.core.network.tokens

import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {
    private var _tokens: TokenServer.Tokens = TokenServer.Tokens(CharArray(0), CharArray(0))
    private val tokens = _tokens

    override fun updateTokens(tokens: TokenServer.Tokens) {
        _tokens = _tokens.copy(accessToken = tokens.accessToken, refreshToken = tokens.refreshToken)
    }

    override fun getAccessToken(): CharArray = tokens.accessToken

    override fun getRefreshToken(): CharArray = tokens.refreshToken

    override fun isTokenEmpty(): Boolean = tokens.isEmpty()
}

interface TokenServer {

    data class Tokens(val accessToken: CharArray, val refreshToken: CharArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Tokens

            if (!accessToken.contentEquals(other.accessToken)) return false
            if (!refreshToken.contentEquals(other.refreshToken)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = accessToken.contentHashCode()
            result = 31 * result + refreshToken.contentHashCode()
            return result
        }

        fun isEmpty(): Boolean = accessToken.isEmpty() && refreshToken.isEmpty()
    }

    /**
     * 토큰을 업데이트한다.
     */
    fun updateTokens(tokens: Tokens)

    /**
     * Access Token을 반환한다.
     */
    fun getAccessToken(): CharArray

    /**
     * Refresh Token을 반환한다.
     */
    fun getRefreshToken(): CharArray

    fun isTokenEmpty(): Boolean

}