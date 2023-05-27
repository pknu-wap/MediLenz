package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.token.ConnectionTokenDto
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    suspend fun signIn(signInParameter: SignInParameter): Flow<Result<ConnectionTokenDto>>
    suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<ConnectionTokenDto>>
    suspend fun reissueTokens(refreshToken: CharArray, email: CharArray): Flow<Result<ConnectionTokenDto>>
}