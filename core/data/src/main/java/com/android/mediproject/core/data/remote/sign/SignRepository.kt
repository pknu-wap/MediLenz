package com.android.mediproject.core.data.remote.sign

import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.requestparameters.SignInParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>>

    fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>>

    suspend fun signOut()

    fun getCurrentTokens(): Flow<TokenState<CurrentTokenDto>>

    val myEmail: Flow<String>
}