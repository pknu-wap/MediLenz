package com.android.mediproject.core.data.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface TokenRepository {
    val session: CognitoUserSession?
}
