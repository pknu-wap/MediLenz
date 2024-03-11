package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter

interface SignDataSource {
    suspend fun logIn(loginParameter: LoginParameter): Result<SignInOutAWS.SignInResponse>
    suspend fun signUp(signUpParameter: SignUpParameter): Result<SignUpAWS.SignUpResponse>
    suspend fun signOut()
}
