package com.android.mediproject.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.NewTokensFromServer
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class TokenDataSourceImpl @Inject constructor(
    private val tokenDataStore: DataStore<SavedToken>,
    private val aesCoder: AesCoder,
    private val tokenServer: TokenServer,
) : TokenDataSource {
    
    private var _currentTokenState: TokenState<CurrentTokens> = TokenState.Empty

    override val currentTokenState: TokenState<CurrentTokens>
        get() = _currentTokenState

    init {
        Log.d("wap", "TokenDataSourceImpl init")

        runBlocking {
            // 앱 프로세스가 첫 시작된 직후에 로컬에 저장된 토큰을 불러온다.
            loadSavedTokens()
        }
    }


    /**
     * 로컬에 저장된 토큰을 불러온다.
     *
     * @return Unit
     *
     * 앱 프로세스가 첫 시작된 직후에 호출된다.
     */
    private suspend fun loadSavedTokens() {
        Log.d("wap", "loadSavedTokens")

        tokenDataStore.data.first().also { savedTokens ->
            if (savedTokens.accessToken.isNotEmpty() || savedTokens.refreshToken.isNotEmpty()) {

                val refreshToken = aesCoder.decode(savedTokens.refreshToken)
                val accessToken = aesCoder.decode(savedTokens.accessToken)

                Log.d("wap", "토큰이 로컬에 저장되어있음")
                Log.d(
                    "wap",
                    "저장된 토큰 정보 -> 액세스 : ${accessToken.joinToString("")}, 갱신 : ${refreshToken.joinToString("")}, 액세스 만료 : ${
                        savedTokens
                            .accessTokenExpiresIn
                    }, 갱신 만료 : ${savedTokens.refreshTokenExpiresIn}",
                )
                tokenServer.tokens.emit(
                    TokenServer.Tokens(
                        refreshToken = refreshToken,
                        accessToken = accessToken,
                        accessTokenExpiresIn = LocalDateTime.parse(savedTokens.accessTokenExpiresIn),
                        refreshTokenExpiresIn = LocalDateTime.parse(savedTokens.refreshTokenExpiresIn),
                    ),
                )
                updateTokenState()
            } else {
                Log.d("wap", "토큰이 로컬에 저장되어있지 않음")
            }
        }
    }


    /**
     * 토큰을 로컬에 저장한다.
     *
     * @param newTokensFromServer 새로 발급받은 토큰
     *
     * 서버에서 응답 받으면 가장 먼저 호출되는 함수이다.
     */
    override suspend fun saveTokensToLocal(newTokensFromServer: NewTokensFromServer) {
        Log.d("wap", "saveTokensToLocal() 호출됨")
        tokenDataStore.updateData { newToken ->
            newToken.toBuilder().setAccessToken(aesCoder.encode(newTokensFromServer.accessToken))
                .setRefreshToken(aesCoder.encode(newTokensFromServer.refreshToken)).let { builder ->
                    val savedToken = tokenServer.currentTokens
                    when (newTokensFromServer.requestBehavior) {
                        is RequestBehavior.NewTokens -> {
                            Log.d("wap", "saveTokensToLocal() : NewTokens")
                            // 새로 모든 토큰을 받았으므로 모든 시각을 저장한다.
                            Log.d(
                                "wap",
                                "새로운 토큰 정보 -> 액세스 : ${newTokensFromServer.accessToken.joinToString("")}, 갱신 : ${
                                    newTokensFromServer
                                        .refreshToken.joinToString("")
                                }," +
                                    " 액세스 만료" +
                                    " " +
                                    ": ${newTokensFromServer.accessTokenExpireDateTime}, 갱신 만료 : ${newTokensFromServer.refreshTokenExpireDateTime}",
                            )

                            tokenServer.tokens.emit(newTokensFromServer.toTokens())
                            updateTokenState()

                            builder.setAccessTokenExpiresIn(newTokensFromServer.accessTokenExpireDateTime.toString())
                                .setRefreshTokenExpiresIn(newTokensFromServer.refreshTokenExpireDateTime.toString())
                        }

                        is RequestBehavior.ReissueTokens -> {
                            Log.d("wap", "saveTokensToLocal() : ReissueTokens")
                            // 액세스 만료 시각만 저장한다.
                            Log.d(
                                "wap",
                                "새로운 토큰 정보 -> 액세스 : ${newTokensFromServer.accessToken.joinToString("")}, 갱신 : ${
                                    newTokensFromServer
                                        .refreshToken.joinToString("")
                                }}, 액세스 만료" + " " +
                                    ": ${newTokensFromServer.accessTokenExpireDateTime}, 갱신 만료 : ${
                                        (savedToken as EndpointTokenState.SavedToken).token.refreshTokenExpiresIn
                                    }",
                            )

                            tokenServer.tokens.emit(
                                TokenServer.Tokens(
                                    refreshToken = newTokensFromServer.refreshToken,
                                    accessToken = newTokensFromServer.accessToken,
                                    accessTokenExpiresIn = newTokensFromServer.accessTokenExpireDateTime,
                                    refreshTokenExpiresIn = savedToken.token.refreshTokenExpiresIn,
                                ),
                            )

                            updateTokenState()
                            builder.setAccessTokenExpiresIn(newTokensFromServer.accessTokenExpireDateTime.toString())
                        }

                    }
                }.build()
        }
    }

    /**
     * 로컬에 저장된 토큰을 모두 제거한다.
     */
    override suspend fun removeTokens() {
        Log.d("wap", "removeTokens() 호출됨")
        tokenServer.removeToken()
        tokenDataStore.updateData {
            it.toBuilder().clear().build()
        }
        updateTokenState()
    }

    private fun updateTokenState() {
        Log.d("wap", "updateTokenState() 호출됨")
        val tokenState = tokenServer.currentTokens
        Log.d("wap", "updateTokenState : $tokenState")

        when (tokenState) {
            is EndpointTokenState.SavedToken -> {
                Log.d(
                    "wap",
                    "현재 토큰 정보 -> 액세스 : ${tokenState.token.accessToken.joinToString("")}, 갱신 : ${
                        tokenState.token.refreshToken
                            .joinToString("")
                    }, 액세스 만료 : ${
                        tokenState.token.accessTokenExpiresIn
                    }, 갱신 만료 : ${tokenState.token.refreshTokenExpiresIn}",
                )

                // 만료 확인
                val currentToken = tokenState.token
                val now = LocalDateTime.now()

                if (now > currentToken.accessTokenExpiresIn) {
                    Log.d("wap", "updateTokenState() : 액세스 만료")
                    // 액세스 만료
                    _currentTokenState = TokenState.AccessExpiration(
                        CurrentTokens(
                            accessToken = currentToken.accessToken,
                            refreshToken = currentToken.refreshToken,
                        ),
                    )
                } else if (now > currentToken.refreshTokenExpiresIn) {
                    Log.d("wap", "updateTokenState() : 리프레시 만료")
                    // 리프레시 만료
                    _currentTokenState = TokenState.RefreshExpiration(
                        CurrentTokens(
                            accessToken = currentToken.accessToken,
                            refreshToken = currentToken.refreshToken,
                        ),
                    )
                } else {
                    // 토큰 유효
                    Log.d("wap", "updateTokenState() : 토큰 유효")
                    _currentTokenState =
                        TokenState.Valid(CurrentTokens(accessToken = currentToken.accessToken, refreshToken = currentToken.refreshToken))
                }
            }

            is EndpointTokenState.NoToken -> {
                Log.d("wap", "updateTokenState : NoToken")
                _currentTokenState = TokenState.Empty
            }
        }
    }

}
