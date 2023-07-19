package com.android.mediproject.core.network.datasource.tokens

import android.util.Log
import com.android.mediproject.core.model.awscommon.BaseAwsSignResponse
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.network.tokens.TokenServer
import retrofit2.Response

/**
 * 서버의 응답을 처리하고, 서버에 토큰을 요청한 이유에 따라 토큰을 업데이트한다.
 *
 * @return Result<T>
 */
internal suspend inline fun <reified T : BaseAwsSignResponse> Response<T>.onResponseWithTokens(
    requestBehavior: RequestBehavior,
    tokenServer: TokenServer,
): Result<T> = run {
    if (isSuccessful) {
        body()?.let { body ->
            if (body.isSuccess()) {
                Log.d("wap", "onResponseWithTokens, tokenServer.saveTokens")
                tokenServer.saveTokens(
                    NewTokensFromServer(
                        accessToken = body.accessToken!!.toCharArray(),
                        refreshToken = body.refreshToken!!.toCharArray(),
                        requestBehavior = requestBehavior,
                    ),
                )
                Result.success(body)
            } else {
                Result.failure(Throwable(body.message))
            }
        } ?: Result.failure(Throwable("Response Body is Null"))
    } else Result.failure(Throwable(errorBody()?.string() ?: "Response Error"))
}
