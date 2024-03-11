package com.android.mediproject.core.domain

import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUseCase @Inject constructor(
    private val signRepository: SignRepository, private val userInfoRepository: UserInfoRepository,
) {
    suspend fun login(loginParameter: LoginParameter) = signRepository.login(loginParameter)

    suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean> = signRepository.signUp(signUpParameter)

    suspend fun signOut() = signRepository.signOut()

    val savedEmail = userInfoRepository.myAccountInfo.map {
        if (it is AccountState.SignedIn) it.email else ""
    }
}
