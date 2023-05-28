package com.android.mediproject.core.datastore

import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 로컬에 토큰 정보를 저장/삭제하는 클래스
 *
 * @property tokenDataStore
 * @property aesCoder
 * @property tokenServer
 *
 * 로컬 토큰 서버에 토큰을 저장/삭제 하고,
 * 보조 기억 장치에 토큰을 저장/삭제 한다.
 */
class TokenDataSourceImpl @Inject constructor(
    private val tokenDataStore: DataStore<ConnectionToken>,
    private val aesCoder: AesCoder,
    private val tokenServer: TokenServer,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : TokenDataSource {

    @OptIn(DelicateCoroutinesApi::class) private val globalScope = GlobalScope

    /**
     * 보조 기억 장치에 저장된 토큰을 가져온다.
     */
    @OptIn(DelicateCoroutinesApi::class) override val savedTokens = tokenDataStore.data.map { savedToken ->
        savedToken.takeIf {
            it.accessToken.isNotEmpty() && it.refreshToken.isNotEmpty() && it.expirationDatetime.isNotEmpty() && LocalDateTime.parse(it.expirationDatetime) > LocalDateTime.now()
        }?.let {
            TokenState.Valid(
                CurrentTokenDto(
                    aesCoder.decode(it.accessToken), aesCoder.decode(it.refreshToken), LocalDateTime.parse(it.expirationDatetime)
                )
            )
        } ?: TokenState.Empty
    }.flowOn(ioDispatcher).buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST).shareIn(
        globalScope, replay = 1, started = SharingStarted.Lazily
    )

    /**
     * 현재 토큰 상태를 가져온다.
     * @return TokenState
     */
    override val currentTokens: TokenState<CurrentTokenDto> = tokenServer.let {
        if (it.isTokenEmpty()) TokenState.Empty
        else if (it.isExpiredAccessToken()) {
            val token = it.tokens
            TokenState.Expiration(
                CurrentTokenDto(
                    token.accessToken.copyOf(), token.refreshToken.copyOf(), token.expirationTimeOfAccessToken
                )
            )
        } else {
            val token = it.tokens
            TokenState.Valid(
                CurrentTokenDto(
                    token.accessToken.copyOf(), token.refreshToken.copyOf(), token.expirationTimeOfAccessToken
                )
            )
        }
    }

    init {
        suspend {
            savedTokens.collectLatest {
                when (it) {
                    is TokenState.Valid -> {
                        tokenServer.tokens = TokenServer.Tokens(
                            accessToken = it.data.accessToken.copyOf(),
                            refreshToken = it.data.refreshToken.copyOf(),
                            expirationTimeOfAccessToken = it.data.expirationTimeOfAccessToken,
                            expirationTimeOfRefreshToken = it.data.expirationTimeOfAccessToken
                        )
                    }

                    else -> {
                        tokenServer.clearTokens()
                    }
                }
            }
        }
    }

    /**
     * 토큰을 저장한다.
     */
    override suspend fun updateTokens(newTokensFromAws: NewTokensFromAws) {
        // refreshToken 변경여부 확인
        if (tokenServer.tokens.refreshToken.contentEquals(newTokensFromAws.refreshToken)) {
            // refreshToken이 변경되지 않았다면, refreshToken은 유지하고 accessToken만 변경한다.
            tokenServer.tokens = tokenServer.tokens.copy(
                accessToken = newTokensFromAws.accessToken, expirationTimeOfAccessToken = LocalDateTime.now()
            )
        } else {
            // refreshToken이 변경되었다면, refreshToken과 accessToken을 모두 변경한다.
            tokenServer.tokens = tokenServer.tokens.copy(
                accessToken = newTokensFromAws.accessToken,
                refreshToken = newTokensFromAws.refreshToken,
                expirationTimeOfRefreshToken = LocalDateTime.now(),
                expirationTimeOfAccessToken = LocalDateTime.now()
            )
        }

        tokenDataStore.updateData { currentToken ->
            val tokens = tokenServer.tokens
            currentToken.toBuilder().setAccessToken(aesCoder.encode(tokens.accessToken)).setRefreshToken(
                aesCoder.encode(tokens.refreshToken)
            ).setExpirationDatetime(tokens.expirationTimeOfRefreshToken.toString()).build()
        }
    }

    /**
     * 저장된 토큰을 모두 제거한다.
     */
    override suspend fun signOut() {
        tokenServer.clearTokens()
        tokenDataStore.updateData { currentToken ->
            currentToken.toBuilder().setAccessToken("").setRefreshToken("").setExpirationDatetime("").build()
        }
    }
}