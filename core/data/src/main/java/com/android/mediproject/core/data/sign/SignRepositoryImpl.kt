package com.android.mediproject.core.data.sign

import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.sign.SignDataSource

class SignRepositoryImpl(
    private val signDataSource: SignDataSource,
    private val appDataStore: AppDataStore,
    private val userInfoRepository: UserInfoRepository,
) : SignRepository {

    override suspend fun login(loginParameter: LoginParameter): Result<Boolean> {
        val result = signDataSource.logIn(loginParameter)
        result.onSuccess {
            appDataStore.run {
                saveSkipIntro(true)
                userInfoRepository.updateMyAccountInfo(
                    AccountState.SignedIn(
                        myId = 0L,
                        email = loginParameter.email.contentToString(),
                        myNickName = it.userSession.username,
                    ),
                )
                saveMyAccountInfo(
                    userEmail = loginParameter.email.contentToString(),
                    nickName = it.userSession.username,
                    myAccountId = 0L,
                )
            }
        }

        return Result.success(true)
    }

    override suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean> {
        val result = signDataSource.signUp(signUpParameter)
        if (result.isSuccess) {
            appDataStore.saveSkipIntro(true)
        }

        return Result.success(true)
    }

    override suspend fun signOut() = signDataSource.signOut()
}
