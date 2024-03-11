package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter

private const val USER_NAME = "user_name"

class SignDataSourceImpl(
    private val signInOutAWS: SignInOutAWS,
    private val signUpAWS: SignUpAWS,
    private val verifyEmailAWS: VerifyEmailAWS,
) : SignDataSource {

    override suspend fun logIn(loginParameter: LoginParameter) = signInOutAWS.signIn(
        SignInRequest(
            loginParameter.email,
            loginParameter.password,
        ),
    )

    override suspend fun signUp(signUpParameter: SignUpParameter): Result<SignUpResponse> = signUpAWS.signUp(
        SignUpRequest(
            signUpParameter.email,
            signUpParameter.password,
            CognitoUserAttributes().apply {
                addAttribute(USER_NAME, signUpParameter.nickName)
            },
        ),
    )

    override suspend fun vertifyEmail(email: String, code: String): Result<Boolean> = verifyEmailAWS.verifyEmail(email, code)

    override suspend fun signOut() {
        signInOutAWS.signOut()
    }
}
