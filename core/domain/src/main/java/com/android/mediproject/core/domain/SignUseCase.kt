package com.android.mediproject.core.domain

import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUseCase @Inject constructor(
    private val signRepository: SignRepository, private val userInfoRepository: UserInfoRepository,
) {
    suspend fun login(loginParameter: LoginParameter): Result<Boolean> = signRepository.login(loginParameter)

    suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean> = signRepository.signUp(signUpParameter)

    suspend fun signOut() = signRepository.signOut()

    val savedEmail: Flow<String> = channelFlow {
        userInfoRepository.myAccountInfo.collectLatest {
            trySend(if (it is AccountState.SignedIn) it.email else "")
        }
    }
}
