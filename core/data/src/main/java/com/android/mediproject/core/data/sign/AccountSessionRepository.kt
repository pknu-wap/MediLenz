package com.android.mediproject.core.data.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface AccountSessionRepository {
    val session: CognitoUserSession?
    val isSignedIn: Boolean
}
