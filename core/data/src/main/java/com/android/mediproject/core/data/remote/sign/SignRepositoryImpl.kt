package com.android.mediproject.core.data.remote.sign

import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.AccessTokenResponse
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val awsDataSource: SignDataSource
) : SignRepository {
    override suspend fun signIn(email: String, password: String): Flow<Result<SignInResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String, nickname: String): Flow<Result<SignUpResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String): Flow<Result<AccessTokenResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }


}