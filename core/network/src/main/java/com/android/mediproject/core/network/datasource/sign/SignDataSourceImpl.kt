package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.datastore.TokenDataSource
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.base.BaseAwsSignResponse
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import java.lang.ref.WeakReference
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi,
    private val tokenDataSource: TokenDataSource,
) : SignDataSource {

    /**
     * 로그인
     */
    override fun signIn(signInParameter: SignInParameter): Flow<Result<SignInResponse>> = channelFlow {
        val email = WeakReference(signInParameter.email.joinToString("")).get()!!
        val password = WeakReference(signInParameter.password.joinToString("")).get()!!

        awsNetworkApi.signIn(SignInRequestParameter(email, password)).onResponseWithTokens(RequestBehavior.NewTokens).fold(onSuccess = {
            Result.success(it)
        }, onFailure = {
            Result.failure(it)
        }).also {
            trySend(it)
        }
    }

    /**
     * 회원가입
     */
    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>> = channelFlow {
        val email = WeakReference(signUpParameter.email.joinToString("")).get()!!
        val password = WeakReference(signUpParameter.password.joinToString("")).get()!!

        awsNetworkApi.signUp(SignUpRequestParameter(email, password, signUpParameter.nickName))
            .onResponseWithTokens(RequestBehavior.NewTokens).fold(onSuccess = {
                Result.success(it)
            }, onFailure = { Result.failure(it) }).also {
                trySend(it)
            }
    }


    /**
     * 토큰 갱신
     */
    override fun reissueTokens(refreshToken: CharArray): Flow<Result<ReissueTokenResponse>> = channelFlow {
        awsNetworkApi.reissueTokens().onResponseWithTokens(RequestBehavior.ReissueTokens).fold(onSuccess = {
            Result.success(it)
        }, onFailure = { Result.failure(it) }).also {
            trySend(it)
        }
    }


    /**
     * 서버의 응답을 처리하고, 서버에 토큰을 요청한 이유에 따라 토큰을 업데이트한다.
     *
     * @return Result<T>
     */
    private suspend inline fun <reified T : BaseAwsSignResponse> Response<T>.onResponseWithTokens(requestBehavior: RequestBehavior): Result<T> =
        if (isSuccessful) {
            body()?.let { body ->
                if (body.isSuccess()) {
                    // 토큰 저장
                    tokenDataSource.saveTokensToLocal(NewTokensFromAws(accessToken = body.accessToken!!.toCharArray(),
                        refreshToken = body.refreshToken!!.toCharArray(),
                        requestBehavior = requestBehavior))
                    Result.success(body)
                } else {
                    Result.failure(Throwable(body.message))
                }
            } ?: Result.failure(Throwable("Response Body is Null"))
        } else Result.failure(Throwable(errorBody()?.string() ?: "Response Error"))


}