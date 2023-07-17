package com.android.mediproject.core.domain.sign

import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.data.remote.user.UserInfoRepository
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUseCase @Inject constructor(
    private val signRepository: SignRepository, private val userInfoRepository: UserInfoRepository
) {
    suspend fun login(loginParameter: LoginParameter): Flow<Result<Unit>> = signRepository.login(loginParameter)

    suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = signRepository.signUp(signUpParameter)

    suspend fun signOut() = signRepository.signOut()

    val savedEmail: Flow<String> = channelFlow {
        userInfoRepository.myAccountInfo.collectLatest {
            if (it is AccountState.SignedIn) {
                trySend(it.email)
            } else {
                trySend("")
            }
        }
    }
}
