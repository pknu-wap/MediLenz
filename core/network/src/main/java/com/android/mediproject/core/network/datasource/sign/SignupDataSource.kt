package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult

interface SignupDataSource {
    suspend fun signUp(signUpParameter: SignUpRequest): Result<SignUpResponse>
    suspend fun confirmEmail(email: String, code: String): Result<Unit>
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
