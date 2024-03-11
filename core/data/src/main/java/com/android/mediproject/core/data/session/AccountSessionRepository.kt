package com.android.mediproject.core.data.session

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface AccountSessionRepository {
    val session: CognitoUserSession?
    val signedIn: Boolean
    suspend fun updateSession(session: CognitoUserSession?)
    suspend fun updateAccount(email: String, nickName: String)
}
