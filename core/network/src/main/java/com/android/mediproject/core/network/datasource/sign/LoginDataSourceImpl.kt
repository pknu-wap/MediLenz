package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class LoginDataSourceImpl(
    private val userPool: CognitoUserPool,
) : LoginDataSource {

    override suspend fun login(request: LoginRequest) = suspendCoroutine { continuation ->
        userPool.getUser(request.email).getSession(
            object : AuthenticationHandler {
                override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                    continuation.resume(Result.success(LoginResponse(userSession, newDevice)))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                    // UserNotConfirmedException : 이메일 인증을 하지 않았을 때 발생
                    // UserExistsException : 이미 가입된 이메일일 때 발생
                }

                override fun authenticationChallenge(continuation: ChallengeContinuation?) {

                }

                override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
                    authenticationContinuation.run {
                        val authDetails = AuthenticationDetails(
                            userId,
                            request.password.decodeToString(),
                            null,
                        )
                        setAuthenticationDetails(authDetails)
                        continueTask()
                    }
                }

                override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {

                }
            },
        )
    }

    override suspend fun logout() = userPool.currentUser.signOut()
}
