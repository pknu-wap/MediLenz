package com.android.mediproject.core.network.datasource.tokens

import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.tokens.TokenServer
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 서버에 토큰 재발급을 요청하는 DataSource
 *
 * @property awsNetworkApi
 * @property tokenServer
 */
@Singleton
class TokenDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi,
    private val tokenServer: TokenServer,
) : TokenDataSource {

    private val mutex = Mutex()
    private var processingTokenReissuance = false

    private val lastTokenReissueResult = MutableSharedFlow<Result<Unit>>(
        replay = 0, extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    /**
     * 토큰 갱신
     */
    private fun reissueTokens(refreshToken: CharArray): Flow<Result<ReissueTokenResponse>> = channelFlow {
        awsNetworkApi.reissueTokens().onResponseWithTokens(RequestBehavior.ReissueTokens, tokenServer).fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) },
        ).also {
            trySend(it)
        }
    }

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
    override fun reissueToken(currentToken: TokenState.Tokens.AccessExpiration<CurrentTokens>): Flow<Result<Unit>> = channelFlow {
        if (processingTokenReissuance) {
            // 이미 토큰 재발급 처리 중인 경우, 기다렸다가 현재 처리 중인 토큰 재발급의 결과를 반환한다.
            lastTokenReissueResult.collect {
                trySend(it)
            }
        }

        mutex.withLock { processingTokenReissuance = true }
        reissueTokens(currentToken.data.refreshToken).collectLatest { reissueTokenResponseResult ->
            val result = reissueTokenResponseResult.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
            lastTokenReissueResult.emit(result)
            trySend(result)

            mutex.withLock { processingTokenReissuance = false }
        }
    }
}
