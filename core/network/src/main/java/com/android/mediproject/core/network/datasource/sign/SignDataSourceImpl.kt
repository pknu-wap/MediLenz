package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.datastore.TokenDataSource
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.base.BaseAwsSignResponse
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.NewTokensFromAws
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.ref.WeakReference
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi,
    private val tokenDataSource: TokenDataSource,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : SignDataSource {

    /**
     * 로그인
     */
    override suspend fun signIn(signInParameter: SignInParameter): Flow<Result<SignInResponse>> = channelFlow {
        val email = WeakReference(signInParameter.email.joinToString(""))
        val password = WeakReference(signInParameter.password.joinToString(""))

        awsNetworkApi.signIn(SignInRequestParameter(email = email.get()!!, password = password.get()!!)).onResponse().fold(onSuccess = {
            Result.success(it)
        }, onFailure = {
            Result.failure(it)
        }).also {
            email.clear()
            password.clear()
            trySend(it)
        }
    }

    /**
     * 회원가입
     */
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>> = channelFlow {
        val email = WeakReference(signUpParameter.email.joinToString(""))
        val password = WeakReference(signUpParameter.password.joinToString(""))

        awsNetworkApi.signUp(SignUpRequestParameter(email.get()!!, password.get()!!, signUpParameter.nickName)).onResponse()
            .fold(onSuccess = {
                Result.success(it)
            }, onFailure = { Result.failure(it) }).also {
                email.clear()
                password.clear()
                trySend(it)
            }
    }


    /**
     * 토큰 갱신
     */
    override suspend fun reissueTokens(refreshToken: CharArray): Flow<Result<ReissueTokenResponse>> = channelFlow {
        awsNetworkApi.reissueTokens().onResponse().fold(onSuccess = {
            Result.success(it)
        }, onFailure = { Result.failure(it) }).also {
            trySend(it)
        }
    }


    /**
     * Response 처리하고, 정상 응답이면 토큰을 업데이트한다.
     *
     * @return Result<T>
     */
    private suspend inline fun <reified T : BaseAwsSignResponse> Response<T>.onResponse(): Result<T> = if (isSuccessful) {
        body()?.let { body ->
            if (body.isSuccess()) {
                // 새로운 토큰 업데이트
                    tokenDataSource.updateTokens(
                        NewTokensFromAws(
                            accessToken = body.accessToken!!.toCharArray(),
                            refreshToken = body.refreshToken!!.toCharArray(),
                        )
                    )
                Result.success(body)
            } else {
                Result.failure(Throwable(body.message))
            }
        } ?: Result.failure(Throwable("Response Body is Null"))
    } else Result.failure(Throwable(errorBody()?.string() ?: "Response Error"))

}