package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.AccessTokenResponse
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    suspend fun signIn(email: String, password: String): Flow<Result<SignInResponse>>
    suspend fun signUp(email: String, password: String, nickname: String): Flow<Result<SignUpResponse>>
    suspend fun reissueTokens(refreshToken: String): Flow<Result<AccessTokenResponse>>
}