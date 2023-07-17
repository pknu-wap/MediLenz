package com.android.mediproject.core.network.datasource.tokens

import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.TokenState
import kotlinx.coroutines.flow.Flow

interface TokenDataSource {

    /**
     * 메모리상에 있는 현재 토큰의 상태에 따라 새로운 토큰을 서버에 요청한다.
     *
     * refresh token이 없는 경우, 토큰 재발급 불가
     *
     * return Result<Unit>.failure()
     *
     * refresh token이 있는 경우, 토큰 재발급 가능
     *
     * return  Result<Unit>.success()
     */
    fun reissueToken(currentToken: TokenState.Tokens.AccessExpiration<CurrentTokens>): Flow<Result<Unit>>
}
