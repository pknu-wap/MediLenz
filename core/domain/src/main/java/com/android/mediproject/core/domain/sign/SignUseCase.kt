package com.android.mediproject.core.domain.sign

import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUseCase @Inject constructor(
    private val signRepository: SignRepository
) {
    suspend fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>> = signRepository.signIn(signInParameter)

    suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = signRepository.signUp(signUpParameter)

    suspend fun signOut() = signRepository.signOut()
}