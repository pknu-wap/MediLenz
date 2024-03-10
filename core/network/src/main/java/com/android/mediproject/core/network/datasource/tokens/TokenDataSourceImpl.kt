package com.android.mediproject.core.network.datasource.tokens

/**

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
private fun reissueTokens(): Flow<Result<ReissueTokenResponse>> = channelFlow {
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
Log.d("wap", "reissueToken, currentToken : $currentToken")
if (processingTokenReissuance) {
lastTokenReissueResult.collect {
trySend(it)
}
}

mutex.withLock { processingTokenReissuance = true }
reissueTokens().collectLatest { reissueTokenResponseResult ->
val result = reissueTokenResponseResult.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })

Log.d("wap", "reissueToken, result : $result")
lastTokenReissueResult.emit(result)
trySend(result)

mutex.withLock { processingTokenReissuance = false }
}
}
}
 */
