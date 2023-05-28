package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    suspend fun signIn(signInParameter: SignInParameter): Flow<Result<SignInResponse>>
    suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>>
    suspend fun reissueTokens(refreshToken: CharArray): Flow<Result<ReissueTokenResponse>>
}