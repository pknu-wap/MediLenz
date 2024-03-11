package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SignupDataSourceImpl(
    private val userPool: CognitoUserPool,
) : SignupDataSource {

    override suspend fun signUp(request: SignUpRequest) = suspendCoroutine { continuation ->
        userPool.signUp(
            request.email, request.passwordString, request.attr, null,
            object : SignUpHandler {
                override fun onSuccess(cognitoUser: CognitoUser, signUpResult: SignUpResult) {
                    val response = SignUpResponse(cognitoUser, signUpResult)
                    continuation.resume(Result.success(response))
                }

                override fun onFailure(exception: Exception) {
                    continuation.resumeWithException(exception)
                    // UserExistsException : 이미 가입된 이메일일 때 발생
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

    override suspend fun confirmEmail(email: String, code: String) = suspendCoroutine {
        userPool.getUser(email).confirmSignUp(
            code, true,
            object : GenericHandler {
                override fun onSuccess() {
                    it.resume(Result.success(Unit))
                }

                override fun onFailure(exception: Exception) {
                    it.resumeWithException(exception)
                }
            },
        )
    }

}
