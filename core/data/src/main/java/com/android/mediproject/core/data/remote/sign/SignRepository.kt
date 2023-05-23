package com.android.mediproject.core.data.remote.sign

import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.AccessTokenResponse
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    suspend fun signIn(email: String, password: String): Flow<Result<SignInResponse>>

    suspend fun signUp(email: String, password: String, nickname: String): Flow<Result<SignUpResponse>>

    suspend fun refreshToken(refreshToken: String): Flow<Result<AccessTokenResponse>>

    suspend fun signOut(): Flow<Result<Unit>>
}