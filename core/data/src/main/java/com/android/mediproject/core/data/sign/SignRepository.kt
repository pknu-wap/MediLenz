package com.android.mediproject.core.data.sign


import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter

interface SignRepository {
    fun login(loginParameter: LoginParameter): Result<Boolean>

    fun signUp(signUpParameter: SignUpParameter): Result<Boolean>

    fun signOut()
}
