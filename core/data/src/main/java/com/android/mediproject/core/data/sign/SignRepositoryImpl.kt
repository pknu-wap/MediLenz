package com.android.mediproject.core.data.sign

import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.requestparameters.LoginParameter

import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class SignRepositoryImpl(
    private val signDataSource: SignDataSource,
    private val appDataStore: AppDataStore,
    private val userInfoRepository: UserInfoRepository,
) : SignRepository {

    override fun login(loginParameter: LoginParameter): Result<Boolean> {
        signDataSource.logIn(loginParameter).collect { signInResult ->
            if (signInResult.isFailure) {
                trySend(Result.failure(signInResult.exceptionOrNull() ?: Exception("로그인 실패")))
            } else {
                appDataStore.apply {
                    saveSkipIntro(true)
                    signInResult.onSuccess {
                        userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                        saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                    }
                }
                trySend(Result.success(Unit))
            }
        }
    }

    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = channelFlow {
        signDataSource.signUp(signUpParameter).collect { signUpResult ->
            if (signUpResult.isFailure) {
                trySend(Result.failure(signUpResult.exceptionOrNull() ?: Exception("로그인 실패")))
                return@collect
            } else {
                appDataStore.apply {
                    saveSkipIntro(true)
                    signUpResult.onSuccess {
                        // 내 계정 정보 메모리에 저장
                        userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                        saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                    }
                }
                trySend(Result.success(Unit))
            }
        }
    }

    /**
     * 로그아웃
     *
     * 저장된 토큰 정보를 삭제한다.
     */
    override fun signOut() = signDataSource.signOut()

}
