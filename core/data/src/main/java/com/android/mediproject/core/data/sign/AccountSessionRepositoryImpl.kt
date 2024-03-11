package com.android.mediproject.core.data.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.android.mediproject.core.datastore.AppDataStore

class AccountSessionRepositoryImpl(
    private val appDataStore: AppDataStore,
) : AccountSessionRepository {

    private var mutableSession: CognitoUserSession? = null
    override val session: CognitoUserSession? get() = mutableSession

    override val signedIn: Boolean
        get() = session != null

    override suspend fun updateSession(session: CognitoUserSession?) {
        mutableSession = session
        if (session == null) {
            appDataStore.clearMyAccountInfo()
        }
    }

    override suspend fun updateAccount(email: String, nickName: String) {
        appDataStore.saveMyAccountInfo(email, nickName)
    }
}
