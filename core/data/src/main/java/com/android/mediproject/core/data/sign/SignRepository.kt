package com.android.mediproject.core.data.sign


import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter

interface SignRepository {
    suspend fun login(loginParameter: LoginParameter): Result<Boolean>

    suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean>

    suspend fun signOut()
}
