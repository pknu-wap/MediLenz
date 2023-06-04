package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    fun signIn(signInParameter: SignInParameter): Flow<Result<SignInResponse>>
    fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>>
    fun reissueTokens(refreshToken: CharArray): Flow<Result<ReissueTokenResponse>>
}