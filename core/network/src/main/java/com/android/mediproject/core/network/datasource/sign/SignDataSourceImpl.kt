package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.AccessTokenResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi
) : SignDataSource {
    override suspend fun signIn(email: String, password: String): Flow<Result<SignInResponse>> = channelFlow {
        awsNetworkApi.signIn(email, password).onResponse()
            .fold(onSuccess = { trySend(Result.success(it)) }, onFailure = { trySend(Result.failure(it)) })
    }

    override suspend fun signUp(email: String, password: String, nickname: String): Flow<Result<SignUpResponse>> = channelFlow {
        awsNetworkApi.signUp(email, password, nickname).onResponse()
            .fold(onSuccess = { trySend(Result.success(it)) }, onFailure = { trySend(Result.failure(it)) })
    }

    override suspend fun reissueTokens(refreshToken: String): Flow<Result<AccessTokenResponse>> = channelFlow {
        awsNetworkApi.reissueTokens(refreshToken).onResponse()
            .fold(onSuccess = { trySend(Result.success(it)) }, onFailure = { trySend(Result.failure(it)) })
    }
}