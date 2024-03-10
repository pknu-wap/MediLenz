package com.android.mediproject.feature.aws

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool

open class AWSAccountManager(
    protected open val userPool: CognitoUserPool,
) {

}
