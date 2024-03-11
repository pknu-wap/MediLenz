package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class VerifyEmailImpl(userPool: CognitoUserPool) : AWSAccountManager(userPool), VerifyEmailAWS {
    override suspend fun verifyEmail(email: String, code: String) = suspendCoroutine {
        userPool.getUser(email).confirmSignUp(
            code, true,
            object : GenericHandler {
                override fun onSuccess() {
                    it.resume(true)
                }

                override fun onFailure(exception: Exception) {
                    it.resume(false)
                }
            },
        )
    }
}

interface VerifyEmailAWS {
    suspend fun verifyEmail(email: String, code: String): Boolean
}
