package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LoginDataSourceImpl(
    private val userPool: CognitoUserPool,
) : LoginDataSource {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        val session = getSession(request)
        session.fold(
            onSuccess = { userSession ->
                val attr = getUserAttr(userSession)
                return attr.fold(
                    onSuccess = { userAttr ->
                        Result.success(LoginResponse(userSession, userAttr))
                    },
                    onFailure = { Result.failure(it) },
                )
            },
            onFailure = { return Result.failure(it) },
        )
    }

    override suspend fun logout() = userPool.currentUser.signOut()

    private suspend fun getSession(request: LoginRequest) = suspendCoroutine<Result<CognitoUserSession>> {
        userPool.getUser(request.email).getSession(
            object : AuthenticationHandler {
                override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                    it.resume(Result.success(userSession))
                }

                override fun onFailure(exception: Exception) {
                    it.resume(Result.failure(exception))
                    // UserNotConfirmedException : 이메일 인증을 하지 않았을 때 발생
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

    private suspend fun getUserAttr(session: CognitoUserSession) = suspendCoroutine<Result<CognitoUserDetails>> {
        userPool.getUser(session.username).getDetails(
            object : GetDetailsHandler {
                override fun onSuccess(cognitoUserDetails: CognitoUserDetails) {
                    it.resume(Result.success(cognitoUserDetails))
                }

                override fun onFailure(exception: Exception) {
                    it.resume(Result.failure(exception))
                }
            },
        )
    }
}
