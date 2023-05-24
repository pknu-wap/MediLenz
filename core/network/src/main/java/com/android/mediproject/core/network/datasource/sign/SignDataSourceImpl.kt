package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.token.ConnectionTokenDto
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.lang.ref.WeakReference
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi
) : SignDataSource {

    /**
     * 로그인
     */
    override suspend fun signIn(signInParameter: SignInParameter): Flow<Result<ConnectionTokenDto>> = channelFlow {
        signInParameter.also { parameter ->
            val email = WeakReference(parameter.email.toString())
            val password = WeakReference(parameter.password.toString())

            awsNetworkApi.signIn(email.get()!!, password.get()!!).onResponse().fold(onSuccess = {
                if (!it.accessToken.isNullOrBlank() || !it.refreshToken.isNullOrBlank()) trySend(
                    Result.success(
                        ConnectionTokenDto(
                            accessToken = it.accessToken!!.toCharArray(),
                            refreshToken = it.refreshToken!!.toCharArray(),
                            userEmail = signInParameter.email,
                            expirationDateTime = java.time.LocalDateTime.now()
                        )
                    )
                )
                else trySend(Result.failure(Exception("Token is not found")))
            }, onFailure = {
                trySend(Result.failure(it))
            })

            email.clear()
            password.clear()
        }
    }

    /**
     * 회원가입
     */
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<ConnectionTokenDto>> = channelFlow {
        signUpParameter.also { parameter ->
            val email = WeakReference(parameter.email.toString())
            val password = WeakReference(parameter.password.toString())

            awsNetworkApi.signUp(email.get()!!, password.get()!!, parameter.nickName).onResponse().fold(onSuccess = {
                if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) trySend(
                    Result.success(
                        ConnectionTokenDto(
                            accessToken = it.accessToken!!.toCharArray(),
                            refreshToken = it.refreshToken!!.toCharArray(),
                            userEmail = signUpParameter.email,
                            expirationDateTime = java.time.LocalDateTime.now()
                        )
                    )
                )
                else trySend(Result.failure(Exception("Token is not found")))
            }, onFailure = { trySend(Result.failure(it)) })

            email.clear()
            password.clear()
        }
    }

    /**
     * 토큰 갱신
     */
    override suspend fun reissueTokens(refreshToken: CharArray, email: CharArray): Flow<Result<ConnectionTokenDto>> = channelFlow {
        awsNetworkApi.reissueTokens(refreshToken.joinToString()).onResponse().fold(onSuccess = {
            if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) trySend(
                Result.success(
                    ConnectionTokenDto(
                        accessToken = it.accessToken!!.toCharArray(),
                        refreshToken = it.refreshToken!!.toCharArray(),
                        userEmail = email,
                        expirationDateTime = java.time.LocalDateTime.now()
                    )
                )
            )
            else trySend(Result.failure(Exception("Token is not found")))
        }, onFailure = { trySend(Result.failure(it)) })
    }
}