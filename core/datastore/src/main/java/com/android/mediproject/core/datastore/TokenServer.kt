package com.android.mediproject.core.datastore

import java.time.LocalDateTime
import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {

    override var tokens: TokenServer.Tokens? = null
    override val currentTokens: EndpointTokenState
        get() = tokens?.let { EndpointTokenState.SavedToken(it) } ?: EndpointTokenState.NoToken


    override fun removeToken() {
        tokens = null
    }
}

interface TokenServer {
    data class Tokens(
        val accessToken: CharArray,
        val refreshToken: CharArray,
        val accessTokenExpiresIn: LocalDateTime,
        val refreshTokenExpiresIn: LocalDateTime,
    ) {

        fun isEmpty(): Boolean = accessToken.isEmpty() || refreshToken.isEmpty()

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

    }

    var tokens: Tokens?
    val currentTokens: EndpointTokenState
    fun removeToken()
}

/**
 * Retrofit, DataStore와 직접 연결되는 토큰의 상태를 나타내는 클래스
 */
sealed class EndpointTokenState {
    object NoToken : EndpointTokenState()
    data class SavedToken(val token: TokenServer.Tokens) : EndpointTokenState()
}