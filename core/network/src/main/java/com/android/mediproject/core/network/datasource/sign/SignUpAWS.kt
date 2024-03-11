package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class SignUpAWSImpl(
    userPool: CognitoUserPool,
) : AWSAccountManager(userPool), SignUpAWS {

    override suspend fun signUp(request: SignUpRequest) = suspendCoroutine { continuation ->
        userPool.signUp(
            request.email, request.passwordString, request.cognitoUserAttributes, null,
            object : SignUpHandler {
                override fun onSuccess(cognitoUser: CognitoUser, signUpResult: SignUpResult) {
                    val response = SignUpResponse(cognitoUser, signUpResult)
                    continuation.resume(Result.success(response))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            },
        )
    }

    override suspend fun confirmSignUp(cognitoUser: CognitoUser, confirmationCode: String) = suspendCoroutine { continuation ->
        cognitoUser.confirmSignUpInBackground(
            confirmationCode, false,
            object : GenericHandler {
                override fun onSuccess() {
                    continuation.resume(Result.success(Unit))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            },
        )
    }

    override suspend fun resendConfirmationCode(cognitoUser: CognitoUser): Result<ConfirmationCodeDeliveryDetails> =
        suspendCoroutine { continuation ->
            cognitoUser.resendConfirmationCode(
                object : VerificationHandler {
                    override fun onSuccess(verificationCodeDeliveryMedium: CognitoUserCodeDeliveryDetails) {
                        continuation.resume(
                            Result.success(
                                ConfirmationCodeDeliveryDetails(
                                    destination = verificationCodeDeliveryMedium.destination,
                                    deliveryMedium = verificationCodeDeliveryMedium.deliveryMedium,
                                    attributeName = verificationCodeDeliveryMedium.attributeName,
                                ),
                            ),
                        )
                    }

                    override fun onFailure(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                },
            )
        }

}

interface SignUpAWS {

    suspend fun signUp(request: SignUpRequest): Result<SignUpResponse>

    suspend fun confirmSignUp(cognitoUser: CognitoUser, confirmationCode: String): Result<Unit>

    suspend fun resendConfirmationCode(cognitoUser: CognitoUser): Result<ConfirmationCodeDeliveryDetails>
}

class SignUpRequest(
    val email: String,
    private val password: ByteArray,
    val cognitoUserAttributes: CognitoUserAttributes,
) {
    val passwordString: String get() = password.decodeToString()
}

data class SignUpResponse(
    val cognitoUser: CognitoUser,
    val signUpResult: SignUpResult,
)

data class ConfirmationCodeDeliveryDetails(
    val destination: String,
    val deliveryMedium: String,
    val attributeName: String,
)
