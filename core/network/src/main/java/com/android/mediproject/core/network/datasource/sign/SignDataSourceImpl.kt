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
        val email = WeakReference(signInParameter.email.toString())
        val password = WeakReference(signInParameter.password.toString())

        awsNetworkApi.signIn(email.get()!!, password.get()!!).onResponse().fold(onSuccess = { response ->
            if (!response.accessToken.isNullOrBlank() || !response.refreshToken.isNullOrBlank()) Result.success(
                ConnectionTokenDto(
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
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<ConnectionTokenDto>> = channelFlow {
        val email = WeakReference(signUpParameter.email.toString())
        val password = WeakReference(signUpParameter.password.toString())

        awsNetworkApi.signUp(email.get()!!, password.get()!!, signUpParameter.nickName).onResponse().fold(onSuccess = {
            if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) Result.success(
                ConnectionTokenDto(
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
    override suspend fun reissueTokens(refreshToken: CharArray, email: CharArray): Flow<Result<ConnectionTokenDto>> = channelFlow {
        awsNetworkApi.reissueTokens(refreshToken.joinToString()).onResponse().fold(onSuccess = {
            if (it.accessToken.isNullOrEmpty() || it.refreshToken.isNullOrEmpty()) Result.success(
                ConnectionTokenDto(
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
}