package com.android.mediproject.core.data.sign


import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    fun login(loginParameter: LoginParameter): Flow<Result<Unit>>

    fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>>

    fun signOut()

}
