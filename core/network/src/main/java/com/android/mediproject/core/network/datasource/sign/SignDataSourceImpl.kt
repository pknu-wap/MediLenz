package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter

class SignDataSourceImpl(
    private val signInOutAWS: SignInOutAWS,
    private val signUpAWS: SignUpAWS,
) : SignDataSource {

    override suspend fun logIn(loginParameter: LoginParameter): Result<SignInOutAWS.SignInResponse> = signInOutAWS.signIn(
        SignInOutAWS.SignInRequest(
            loginParameter.email.contentToString(),
            loginParameter.password.map { it.code.toByte() }.toByteArray(),
        ),
    )

    override suspend fun signUp(signUpParameter: SignUpParameter): Result<SignUpAWS.SignUpResponse> = signUpAWS.signUp(
        SignUpAWS.SignUpRequest(
            signUpParameter.email.contentToString(),
            signUpParameter.password.map { it.code.toByte() }.toByteArray(),
            CognitoUserAttributes().apply {
                addAttribute("user_name", signUpParameter.nickName)
            },
        ),
    )

    override suspend fun signOut() {
        signInOutAWS.signOut()
    }
}
