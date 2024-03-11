package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class SignInOutAWSImpl(userPool: CognitoUserPool) : AWSAccountManager(userPool), SignInOutAWS {

    override suspend fun signIn(request: SignInRequest) = suspendCancellableCoroutine { continuation ->
        userPool.getUser(request.email).getSession(
            object : AuthenticationHandler {
                override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                    continuation.resume(SignInState.Success(userSession, newDevice))
                }

                override fun onFailure(exception: Exception) {
                    if (exception is UserNotConfirmedException) {
                        continuation.resume(SignInState.NotVerified)
                    } else {
                        continuation.resume(SignInState.Failed(exception))
                    }
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
    suspend fun signIn(request: SignInRequest): SignInState

    suspend fun signOut()
}

class SignInRequest(
    val email: String,
    val password: ByteArray,
)


sealed interface SignInState {
    class Success(
        val userSession: CognitoUserSession,
        val device: CognitoDevice? = null,
    ) : SignInState

    data class Failed(val exception: Exception) : SignInState
    data object NotVerified : SignInState
}
