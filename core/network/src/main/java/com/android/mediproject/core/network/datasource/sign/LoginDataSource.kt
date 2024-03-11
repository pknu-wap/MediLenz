package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
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
    val newDevice: CognitoDevice?,
)
