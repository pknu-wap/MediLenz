package com.android.mediproject.core.data.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.sign.SignDataSource

internal class SignRepositoryImpl(
    private val signDataSource: SignDataSource,
    private val appDataStore: AppDataStore,
    private val userInfoRepository: UserInfoRepository,
) : SignRepository, AccountSessionRepository {

    private var _session: CognitoUserSession? = null
    override val session: CognitoUserSession? get() = _session

    override val isSignedIn: Boolean
        get() = session != null


    override suspend fun login(loginParameter: LoginParameter) = signDataSource.logIn(loginParameter).fold(
        onSuccess = {
            _session = it.userSession
            appDataStore.run {
                saveSkipIntro(true)
                userInfoRepository.updateMyAccountInfo(
                    AccountState.SignedIn(
                        myId = 0L,
                        email = loginParameter.email,
                        myNickName = it.userSession.username,
                    ),
                )
                saveMyAccountInfo(
                    userEmail = loginParameter.email,
                    nickName = it.userSession.username,
                    myAccountId = 0L,
                )
            }
            SignInState.Success
        },
        onFailure = {
            if (it is UserNotConfirmedException) {
                SignInState.NotVerified
            } else {
                SignInState.Failed(it)
            }
        },
    )


    override suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean> {
        val result = signDataSource.signUp(signUpParameter)
        if (result.isSuccess) {
            appDataStore.saveSkipIntro(true)
        }
        return Result.success(true)
    }

    override suspend fun signOut() {
        _session = null
        signDataSource.signOut()
    }
}


sealed interface SignInState {
    data object Success : SignInState
    data object NotVerified : SignInState
    data class Failed(val exception: Throwable) : SignInState
}
