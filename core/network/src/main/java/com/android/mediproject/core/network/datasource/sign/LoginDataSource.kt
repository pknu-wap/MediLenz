package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface LoginDataSource {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun logout()
}

class LoginRequest(
    val email: String,
    val password: ByteArray,
)

class LoginResponse(
    val userSession: CognitoUserSession,
    val attr: CognitoUserDetails,
    val newDevice: CognitoDevice? = null,
)
