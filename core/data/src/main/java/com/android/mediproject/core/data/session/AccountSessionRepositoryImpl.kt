package com.android.mediproject.core.data.session

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.user.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AccountSessionRepositoryImpl(
    private val appDataStore: AppDataStore,
    private val userPool: CognitoUserPool,
) : AccountSessionRepository {
    override val lastSavedEmail = appDataStore.userEmail.distinctUntilChanged()

    override val userOnCurrentSession = appDataStore.userEmail.combine(appDataStore.nickName) { email, nickName ->
        if (email.isEmpty() || nickName.isEmpty()) {
            null
        } else {
            UserEntity(nickName, email)
        }
    }

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
        appDataStore.saveMyAccountInfo(email, nickName)
    }

    override suspend fun loadSession() {
        suspendCoroutine {
            userPool.currentUser?.getSession(
                object : AuthenticationHandler {
                    override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                        it.resume(userSession)
                    }

                    override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
                    }

                    override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                    }

                    override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                    }

                    override fun onFailure(exception: Exception?) {
                        it.resume(null)
                    }
                },
            )
        }.let { session ->
            mutableSession.value = session
        }
    }

}
