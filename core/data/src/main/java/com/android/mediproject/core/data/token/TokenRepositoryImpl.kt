package com.android.mediproject.core.data.token

import android.util.Log
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.network.datasource.tokens.TokenDataSource
import com.android.mediproject.core.network.tokens.TokenServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val tokenServer: TokenServer,
) : TokenRepository {
    /**
     * 현재 토큰의 상태를 반환한다.
     *
     * 외부에서 인터페이스로 접근할 때, 이 Flow를 collect하면, 토큰의 상태를 받을 수 있다.
     *
     * 만약 호출 했을 때, 만료되었으면 reissueToken()을 자동으로 호출해서 새로운 토큰을 반환한다.
     */
    override fun getCurrentTokens(): Flow<TokenState<CurrentTokens>> = channelFlow {
        when (val tokenState = tokenServer.tokenState) {
            is TokenState.Tokens.AccessExpiration -> {
                // access token이 만료되었으므로 토큰 재발급 요청
                Log.d("wap", "getCurrentTokens: access token이 만료되었으므로 토큰 재발급 요청")
                tokenDataSource.reissueToken(tokenState).collectLatest {
                    send(tokenServer.tokenState)
                }
            }

            else -> {
                /*
                 * @see TokenState.Tokens.RefreshExpiration 리프레시 만료
                 * @see TokenState.Tokens.Valid 유효
                 * @see TokenState.Empty 토큰이 없음
                 * @see TokenState.Error 에러
                 *
                 * 그대로 반환한다.
                 */
                send(tokenState)
            }
        }
    }

}
