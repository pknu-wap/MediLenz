package com.android.mediproject.core.network.tokens

import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.network.datasource.tokens.NewTokens

internal class TokenServerImpl(
    private val aesCoder: AesCoder, override val tokenState: TokenState<CurrentTokens>,
) : TokenServer {
    override suspend fun saveTokens(newTokens: NewTokens) {
        TODO("Not yet implemented")
    }

    override fun removeTokens() {
        TODO("Not yet implemented")
    }

}


/*

@OptIn(DelicateCoroutinesApi::class)
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
        GlobalScope.launch(Dispatchers.Default) {
            loadSavedTokens()
        }
    }

    */
/**
 * 로컬에 저장된 토큰을 불러온다.
 *
 * TokenServer인스턴스가 생성된 직후에 호출된다.
 *//*

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


    */
/**
 * 토큰을 로컬에 저장한다.
 *
 * @param newTokens 새로 발급받은 토큰
 *
 * 서버에서 응답 받으면 가장 먼저 호출되는 함수이다.
 *//*

    override suspend fun saveTokens(newTokens: NewTokens) {
        tokenDataStore.updateData { newToken ->
            newToken.toBuilder().setAccessToken(aesCoder.encode(newTokens.accessToken))
                .setRefreshToken(aesCoder.encode(newTokens.refreshToken)).let { builder ->
                    when (newTokens.requestBehavior) {
                        is RequestBehavior.NewTokens -> {
                            // 새로운 토큰을 받았으므로 모든 시각을 저장한다.
                            updateTokenState(newTokens.toServerTokens())

                            builder.setAccessTokenExpiresIn(newTokens.accessTokenExpireDateTime.toString())
                                .setRefreshTokenExpiresIn(newTokens.refreshTokenExpireDateTime.toString())
                        }

                        is RequestBehavior.ReissueTokens -> {
                            // 액세스 만료 시각만 저장한다.
                            updateTokenState(
                                TokenServer.Tokens(
                                    refreshToken = newTokens.refreshToken,
                                    accessToken = newTokens.accessToken,
                                    accessTokenExpiresIn = newTokens.accessTokenExpireDateTime,
                                    refreshTokenExpiresIn = refreshTokenExpiresIn,
                                ),
                            )

                            builder.setAccessTokenExpiresIn(newTokens.accessTokenExpireDateTime.toString())
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

    override fun removeTokens() {
        GlobalScope.launch {
            Log.d("wap", "removeTokens() 호출됨")
            _tokenState = TokenState.Empty
            _refreshTokenExpiresIn = null
            _accessTokenExpiresIn = null

            tokenDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}
*/
