package com.android.mediproject.core.data.session

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.user.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AccountSessionRepositoryImpl(
    private val appDataStore: AppDataStore,
) : AccountSessionRepository {
    override val lastSavedEmail = appDataStore.userEmail.distinctUntilChanged()

    private val mutableUserOnCurrentSession = MutableStateFlow<UserEntity?>(null)
    override val userOnCurrentSession = mutableUserOnCurrentSession.asStateFlow()

    private var mutableSession = MutableStateFlow<CognitoUserSession?>(null)
    override val session = mutableSession.asStateFlow()

    override val signedIn: Boolean
        get() = session.value != null

    override suspend fun updateSession(session: CognitoUserSession?) {
        mutableSession.value = session

        if (session == null) {
            appDataStore.clearMyAccountInfo()
        }
    }

    override suspend fun updateAccount(email: String, nickName: String) {
        mutableUserOnCurrentSession.value = UserEntity(email, nickName)
        appDataStore.saveMyAccountInfo(email, nickName)
    }
}
