package com.android.mediproject.feature.aws

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class SignInOutAWSImpl(override val userPool: CognitoUserPool) : AWSAccountManager(userPool), SignInOutAWS {

    override suspend fun signIn(request: SignInOutAWS.SignInRequest) = suspendCancellableCoroutine { continuation ->
        userPool.getUser(request.email).getSession(
            object : AuthenticationHandler {
                override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                    val response = SignInOutAWS.SignInResponse(userSession, newDevice)
                    continuation.resume(Result.success(response))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                    // UserNotConfirmedException : 이메일 인증을 하지 않았을 때 발생
                }

                override fun authenticationChallenge(continuation: ChallengeContinuation?) {

                }

                override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
                    authenticationContinuation.run {
                        val authDetails = AuthenticationDetails(userId, request.passwordString, null)
                        setAuthenticationDetails(authDetails)
                        continueTask()
                    }
                }

                override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {

                }
            },
        )
    }

    override suspend fun signOut() = suspendCancellableCoroutine { continuation ->
        userPool.currentUser.signOut()
        continuation.resume(Result.success(Unit))
    }
}


interface SignInOutAWS {

    suspend fun signIn(request: SignInRequest): Result<SignInResponse>

    suspend fun signOut(): Result<Unit>

    class SignInRequest(
        val email: String,
        private val password: ByteArray,
    ) {
        val passwordString: String get() = password.decodeToString()
    }

    data class SignInResponse(
        val userSession: CognitoUserSession,
        val newDevice: CognitoDevice? = null,
    )
}
