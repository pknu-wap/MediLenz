package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    suspend fun signIn(signInParameter: SignInParameter): Flow<Result<CurrentTokenDto>>
    suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<CurrentTokenDto>>
    suspend fun reissueTokens(refreshToken: CharArray, email: CharArray): Flow<Result<CurrentTokenDto>>
}