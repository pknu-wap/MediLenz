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

internal class SignInOutAWSImpl(userPool: CognitoUserPool) : AWSAccountManager(userPool), SignInOutAWS {

    override suspend fun signIn(request: SignInRequest) = suspendCoroutine { continuation ->
        userPool.getUser(request.email).getSession(
            object : AuthenticationHandler {
                override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                    continuation.resume(Result.success(SignInResponse(userSession, newDevice)))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                    // UserNotConfirmedException : 이메일 인증을 하지 않았을 때 발생
                }

                override fun authenticationChallenge(continuation: ChallengeContinuation?) {

                }

                override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
                    authenticationContinuation.run {
                        val authDetails = AuthenticationDetails(userId, request.password.decodeToString(), null)
                        setAuthenticationDetails(authDetails)
                        continueTask()
                    }
                }

                override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {

                }
            },
        )
    }

    override suspend fun signOut() = userPool.currentUser.signOut()
}


interface SignInOutAWS {
    suspend fun signIn(request: SignInRequest): Result<SignInResponse>

    suspend fun signOut()
}

class SignInRequest(
    val email: String,
    val password: ByteArray,
)

class SignInResponse(
    val userSession: CognitoUserSession,
    val newDevice: CognitoDevice?,
)
