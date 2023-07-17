package com.android.mediproject.core.data.remote.sign


import com.android.mediproject.core.model.requestparameters.SignInParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    fun login(loginParameter: LoginParameter): Flow<Result<Unit>>

    fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>>

    suspend fun signOut()

}
