package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.base.BaseAwsSignResponse
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import com.android.mediproject.core.network.tokens.TokenServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import java.lang.ref.WeakReference
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi, private val tokenServer: TokenServer,
) : SignDataSource {

    /**
     * 로그인
     */
    override suspend fun signIn(signInParameter: SignInParameter): Flow<Result<CurrentTokenDto>> = channelFlow {
        val email = WeakReference(signInParameter.email.joinToString(""))
        val password = WeakReference(signInParameter.password.joinToString(""))

        awsNetworkApi.signIn(SignInRequestParameter(email = email.get()!!, password = password.get()!!)).onResponse()
            .fold(onSuccess = { response ->
                if (!response.accessToken.isNullOrBlank() || !response.refreshToken.isNullOrBlank()) Result.success(
                    CurrentTokenDto(
                        accessToken = response.accessToken!!.toCharArray(),
                        refreshToken = response.refreshToken!!.toCharArray(),
                        userEmail = signInParameter.email,
                        expirationDateTime = java.time.LocalDateTime.now()
                    )
                )
                else Result.failure(Exception("Token is not found"))
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
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<CurrentTokenDto>> = channelFlow {
        val email = WeakReference(signUpParameter.email.joinToString(""))
        val password = WeakReference(signUpParameter.password.joinToString(""))

        awsNetworkApi.signUp(SignUpRequestParameter(email.get()!!, password.get()!!, signUpParameter.nickName)).onResponse()
            .fold(onSuccess = {
                if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) Result.success(
                    CurrentTokenDto(
                        accessToken = it.accessToken!!.toCharArray(),
                        refreshToken = it.refreshToken!!.toCharArray(),
                        userEmail = signUpParameter.email,
                        expirationDateTime = java.time.LocalDateTime.now()
                    )
                )
                else Result.failure(Exception("Token is not found"))
            }, onFailure = { Result.failure(it) }).also {
                email.clear()
                password.clear()
                trySend(it)
            }
    }


    /**
     * 토큰 갱신
     */
    override suspend fun reissueTokens(refreshToken: CharArray, email: CharArray): Flow<Result<CurrentTokenDto>> = channelFlow {
        awsNetworkApi.reissueTokens().onResponse().fold(onSuccess = {
            if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) Result.success(
                CurrentTokenDto(
                    accessToken = it.accessToken!!.toCharArray(),
                    refreshToken = it.refreshToken!!.toCharArray(),
                    userEmail = email,
                    expirationDateTime = java.time.LocalDateTime.now()
                )
            )
            else Result.failure(Exception("Token is not found"))
        }, onFailure = { Result.failure(it) }).also {
            trySend(it)
        }
    }


    /**
     * Response 처리하고, 정상 응답이면 토큰을 업데이트한다.
     *
     * @return Result<T>
     */
    private inline fun <reified T : BaseAwsSignResponse> Response<T>.onResponse(): Result<T> {
        if (isSuccessful) {
            body()?.let { body ->
                // update tokens
                tokenServer.updateTokens(
                    TokenServer.Tokens(
                        accessToken = body.accessToken?.toCharArray() ?: CharArray(0),
                        refreshToken = body.refreshToken?.toCharArray() ?: CharArray(0),
                        email = CharArray(0),
                        expirationDateTime = java.time.LocalDateTime.now()
                    )
                )
                return Result.success(body)
            } ?: return Result.failure(Throwable("Response Body is Null"))
        } else {
            return Result.failure(errorBody()?.string()?.let { Throwable(it) } ?: Throwable("Response Error"))
        }
    }
}