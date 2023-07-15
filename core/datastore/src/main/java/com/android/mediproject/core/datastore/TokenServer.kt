package com.android.mediproject.core.datastore

import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDateTime
import javax.inject.Inject

class TokenServerImpl @Inject constructor() : TokenServer {

    override val tokens = MutableSharedFlow<TokenServer.Tokens?>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 20,
    )

    /**
     * 토큰을 저장할 채널을 열고
     *
     * 토큰이 저장되어있으면 저장된 토큰을 반환하고
     * 저장되어있지 않으면 NoToken을 반환한다.
     */
    override val currentTokens: EndpointTokenState
        get() {
            val token = tokens.replayCache.lastOrNull()
            return if (token == null) {
                EndpointTokenState.NoToken
            } else {
                EndpointTokenState.SavedToken(token)
            }
        }


    override fun removeToken() {
        // 채널에 null을 던져서 토큰을 삭제한다.
        // currentTokens에서 NoToken을 반환하게 된다.
        tokens.tryEmit(null)
    }
}

interface TokenServer {
    data class Tokens(
        val accessToken: CharArray,
        val refreshToken: CharArray,
        val accessTokenExpiresIn: LocalDateTime,
        val refreshTokenExpiresIn: LocalDateTime,
    ) {

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

    val tokens: MutableSharedFlow<Tokens?>
    val currentTokens: EndpointTokenState
    fun removeToken()
}

/**
 * Retrofit, DataStore와 직접 연결되는 토큰의 상태를 나타내는 클래스
 */
sealed interface EndpointTokenState {
    object NoToken : EndpointTokenState
    data class SavedToken(val token: TokenServer.Tokens) : EndpointTokenState
}

fun NewTokensFromAws.toTokens(): TokenServer.Tokens {
    return TokenServer.Tokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresIn = accessTokenExpireDateTime,
        refreshTokenExpiresIn = refreshTokenExpireDateTime,
    )
}
