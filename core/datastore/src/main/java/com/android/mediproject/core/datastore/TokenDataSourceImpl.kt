package com.android.mediproject.core.datastore

import android.util.Log
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
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : TokenDataSource {


    override suspend fun currentTokens(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        if (tokenServer.isTokenEmpty()) {
            val load = async { loadSavedTokens() }
            load.await()
        }
        Log.d("Tgyuu", "currentTokens")
        val token = tokenServer.tokens?.let { tokens ->
            if (tokens.isEmpty())
                TokenState.Empty
            else {
                val currentTokenDto = CurrentTokenDto(
                    refreshToken = tokens.refreshToken.copyOf(),
                    accessToken = tokens.accessToken.copyOf(),
                    expirationTimeOfAccessToken = tokens.expirationTimeOfAccessToken
                )
                Log.d("Tgyuu", "토큰 만료 확인")
                // 토큰 만료 확인
                if (tokenServer.isExpiredAccessToken() && tokenServer.isExpiredRefreshToken())
                    TokenState.Expiration(currentTokenDto)
                else
                    TokenState.Valid(
                        currentTokenDto
                    )
            }
        } ?: TokenState.Empty
        trySend(token)
    }

    private suspend fun loadSavedTokens() {
        Log.d("Tgyuu", "loadSavedTokens")

        return tokenDataStore.data.first().let { savedToken ->
            Log.d("Tgyuu", "saved")
            if (savedToken.accessToken.isNotEmpty() && savedToken.refreshToken.isNotEmpty())
                tokenServer.tokens = TokenServer.Tokens(
                    refreshToken = savedToken.refreshToken.toCharArray(),
                    accessToken = savedToken.accessToken.toCharArray(),
                    expirationTimeOfAccessToken = LocalDateTime.parse(savedToken.expirationDatetime),
                    expirationTimeOfRefreshToken = LocalDateTime.parse(savedToken.expirationDatetime)
                )
        }
    }


    /**
     * 토큰을 저장한다.
     */
    override suspend fun updateTokens(newTokensFromAws: NewTokensFromAws) {
        if (!tokenServer.isTokenEmpty()) {
            // refreshToken 변경여부 확인
            if (tokenServer.currentTokens.refreshToken.contentEquals(newTokensFromAws.refreshToken))
            // refreshToken이 변경되지 않았다면, refreshToken은 유지하고 accessToken만 변경한다.
                tokenServer.tokens = tokenServer.currentTokens.copy(
                    accessToken = newTokensFromAws.accessToken,
                    expirationTimeOfAccessToken = LocalDateTime.now().plusHours(1)
                )
        } else
            tokenServer.tokens = TokenServer.Tokens(accessToken = newTokensFromAws.accessToken,
                refreshToken = newTokensFromAws.refreshToken,
                expirationTimeOfRefreshToken = LocalDateTime.now().plusHours(1),
                expirationTimeOfAccessToken = LocalDateTime.now().plusHours(1)
            )


        tokenDataStore.updateData { currentToken ->
            val tokens = tokenServer.currentTokens
            currentToken.toBuilder().setAccessToken(aesCoder.encode(tokens.accessToken))
                .setRefreshToken(
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
            currentToken.toBuilder().setAccessToken("").setRefreshToken("")
                .setExpirationDatetime("").build()
        }
    }
}