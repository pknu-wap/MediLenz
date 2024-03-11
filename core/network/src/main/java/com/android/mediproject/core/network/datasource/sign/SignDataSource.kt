package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter

interface SignDataSource {
    suspend fun logIn(loginParameter: LoginParameter): Result<SignInResponse>
    suspend fun signUp(signUpParameter: SignUpParameter): Result<SignUpResponse>
    suspend fun vertifyEmail(email: String, code: String): Result<Boolean>
    suspend fun signOut()
}
