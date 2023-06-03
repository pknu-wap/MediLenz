package com.android.mediproject.core.datastore

import java.time.LocalDateTime
import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {
    override var tokens : TokenServer.Tokens? = null

    override val currentTokens: TokenServer.Tokens
        get() = tokens!!

    override fun isTokenEmpty(): Boolean = tokens?.isEmpty()?:true

    override fun isExpiredAccessToken(): Boolean = tokens?.run {
        LocalDateTime.now().isAfter(expirationTimeOfAccessToken)
    } ?: true

    override fun isExpiredRefreshToken(): Boolean =  tokens?.run {
        LocalDateTime.now().isAfter(expirationTimeOfRefreshToken)
    } ?: true

    override fun clearTokens() {
        tokens = TokenServer.Tokens(CharArray(0), CharArray(0), LocalDateTime.now(), LocalDateTime.now())
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

    var tokens: Tokens?
    val currentTokens : Tokens

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