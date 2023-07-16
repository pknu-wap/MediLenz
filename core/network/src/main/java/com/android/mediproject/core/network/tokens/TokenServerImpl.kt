package com.android.mediproject.core.network.tokens

import android.util.Log
import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.datastore.SavedToken
import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.network.datasource.tokens.NewTokensFromServer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import javax.inject.Inject


internal class TokenServerImpl @Inject constructor(
    private val tokenDataStore: DataStore<SavedToken>,
    private val aesCoder: AesCoder,
) : TokenServer {
    private var _tokenState: TokenState<CurrentTokens> = TokenState.Empty

    override val tokenState: TokenState<CurrentTokens>
        get() = _tokenState

    private val expiresMutex = Mutex()
    private var _refreshTokenExpiresIn: LocalDateTime? = null
    private val refreshTokenExpiresIn: LocalDateTime
        get() = _refreshTokenExpiresIn!!

    private var _accessTokenExpiresIn: LocalDateTime? = null
    private val accessTokenExpiresIn: LocalDateTime
        get() = _accessTokenExpiresIn!!

    init {
        @OptIn(DelicateCoroutinesApi::class) GlobalScope.launch(Dispatchers.Default) {
            // 앱 프로세스가 첫 시작된 직후에 로컬에 저장된 토큰을 불러온다.
            loadSavedTokens()
        }
    }

    /**
     * 로컬에 저장된 토큰을 불러온다.
     *
     * TokenServer인스턴스가 생성된 직후에 호출된다.
     */
    private suspend fun loadSavedTokens() {
        Log.d("wap", "loadSavedTokens")

        val savedTokens = tokenDataStore.data.first()
        if (savedTokens.accessToken.isEmpty() || savedTokens.refreshToken.isEmpty()) {
            Log.d("wap", "토큰이 로컬에 저장되어있지 않음")
            return
        }

        Log.d("wap", "토큰이 로컬에 저장되어있음")

        val refreshToken = aesCoder.decode(savedTokens.refreshToken)
        val accessToken = aesCoder.decode(savedTokens.accessToken)

        updateTokenState(
            TokenServer.Tokens(
                refreshToken = refreshToken,
                accessToken = accessToken,
                accessTokenExpiresIn = LocalDateTime.parse(savedTokens.accessTokenExpiresIn),
                refreshTokenExpiresIn = LocalDateTime.parse(savedTokens.refreshTokenExpiresIn),
            ),
        )
    }


    /**
     * 토큰을 로컬에 저장한다.
     *
     * @param newTokensFromServer 새로 발급받은 토큰
     *
     * 서버에서 응답 받으면 가장 먼저 호출되는 함수이다.
     */
    override suspend fun saveTokens(newTokensFromServer: NewTokensFromServer) {
        tokenDataStore.updateData { newToken ->
            newToken.toBuilder().setAccessToken(aesCoder.encode(newTokensFromServer.accessToken))
                .setRefreshToken(aesCoder.encode(newTokensFromServer.refreshToken)).let { builder ->
                    when (newTokensFromServer.requestBehavior) {
                        is RequestBehavior.NewTokens -> {
                            // 새로운 토큰을 받았으므로 모든 시각을 저장한다.
                            updateTokenState(newTokensFromServer.toServerTokens())

                            builder.setAccessTokenExpiresIn(newTokensFromServer.accessTokenExpireDateTime.toString())
                                .setRefreshTokenExpiresIn(newTokensFromServer.refreshTokenExpireDateTime.toString())
                        }

                        is RequestBehavior.ReissueTokens -> {
                            // 액세스 만료 시각만 저장한다.
                            updateTokenState(
                                TokenServer.Tokens(
                                    refreshToken = newTokensFromServer.refreshToken,
                                    accessToken = newTokensFromServer.accessToken,
                                    accessTokenExpiresIn = newTokensFromServer.accessTokenExpireDateTime,
                                    refreshTokenExpiresIn = refreshTokenExpiresIn,
                                ),
                            )

                            builder.setAccessTokenExpiresIn(newTokensFromServer.accessTokenExpireDateTime.toString())
                        }

                    }
                }.build()
        }
    }


    private suspend fun updateTokenState(tokens: TokenServer.Tokens) {
        expiresMutex.withLock {
            _refreshTokenExpiresIn = tokens.refreshTokenExpiresIn
            _accessTokenExpiresIn = tokens.accessTokenExpiresIn
        }

        Log.d("wap", "updateTokenState() 호출됨, $tokens")

        // 만료 확인
        val now = LocalDateTime.now()

        if (now > tokens.accessTokenExpiresIn) {
            // 액세스 만료
            _tokenState = TokenState.Tokens.AccessExpiration(
                CurrentTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                ),
            )
        } else if (now > tokens.refreshTokenExpiresIn) {
            // 리프레시 만료
            _tokenState = TokenState.Tokens.RefreshExpiration(
                CurrentTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                ),
            )
        } else {
            // 토큰 유효
            _tokenState = TokenState.Tokens.Valid(CurrentTokens(accessToken = tokens.accessToken, refreshToken = tokens.refreshToken))
        }

        Log.d("wap", "updateTokenState() 호출됨, tokenState : $tokenState")
    }

    override suspend fun removeTokens() {
        Log.d("wap", "removeTokens() 호출됨")
        _tokenState = TokenState.Empty
        tokenDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}
