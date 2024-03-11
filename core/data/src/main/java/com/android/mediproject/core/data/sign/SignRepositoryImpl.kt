package com.android.mediproject.core.data.sign

import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException
import com.android.mediproject.core.data.session.AccountSessionRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter
import com.android.mediproject.core.network.datasource.sign.LoginDataSource
import com.android.mediproject.core.network.datasource.sign.LoginRequest
import com.android.mediproject.core.network.datasource.sign.SignUpRequest
import com.android.mediproject.core.network.datasource.sign.SignupDataSource

internal class SignRepositoryImpl(
    private val loginDataSource: LoginDataSource,
    private val signupDataSource: SignupDataSource,
    private val accountSessionRepository: AccountSessionRepository,
    private val appDataStore: AppDataStore,
) : SignRepository {

    override suspend fun login(loginParameter: LoginParameter) = loginDataSource.login(
        LoginRequest(
            loginParameter.email,
            loginParameter.password,
        ),
    ).fold(
        onSuccess = {
            accountSessionRepository.updateSession(it.userSession)
            accountSessionRepository.updateAccount(loginParameter.email, it.userSession.username)
            appDataStore.saveSkipIntro(true)
            LoginState.Success
        },
        onFailure = {
            if (it is UserNotConfirmedException) {
                LoginState.NotVerified
            } else {
                LoginState.Failed(it)
            }
        },
    )

    override suspend fun signUp(signUpParameter: SignUpParameter) = signupDataSource.signUp(
        SignUpRequest(
            signUpParameter.email,
            signUpParameter.password,
            signUpParameter.nickName,
        ),
    ).fold(
        onSuccess = {
            appDataStore.saveSkipIntro(true)
            SignUpState.Success
        },
        onFailure = { exception ->
            if (exception is UsernameExistsException) {
                SignUpState.UserExists
            } else {
                SignUpState.Failed(exception)
            }
        },
    )

    override suspend fun logout() {
        accountSessionRepository.updateSession(null)
        loginDataSource.logout()
    }

    override suspend fun confirmEmail(email: String, code: String): Result<Unit> {
        return signupDataSource.confirmEmail(email, code)
    }
}

sealed interface LoginState {
    data object Success : LoginState
    data object NotVerified : LoginState
    data class Failed(val exception: Throwable) : LoginState
}

sealed interface SignUpState {
    data object Success : SignUpState
    data object UserExists : SignUpState

    data class Failed(val exception: Throwable) : SignUpState
}
