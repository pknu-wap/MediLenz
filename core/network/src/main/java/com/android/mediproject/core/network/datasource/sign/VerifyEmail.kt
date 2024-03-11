package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool

class VerifyEmailImpl(userPool: CognitoUserPool) : AWSAccountManager(userPool), VerifyEmailAWS {
    override suspend fun verifyEmail(email: String, code: String): Boolean {
        TODO("Not yet implemented")
    }
}

interface VerifyEmailAWS {
    suspend fun verifyEmail(email: String, code: String): Boolean
}
