package com.android.mediproject.core.data.session

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.android.mediproject.core.model.user.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AccountSessionRepository {
    val lastSavedEmail: Flow<String>
    val userOnCurrentSession: StateFlow<UserEntity?>
    val session: StateFlow<CognitoUserSession?>

    val signedIn: Boolean
    suspend fun updateSession(session: CognitoUserSession?)
    suspend fun updateAccount(email: String, nickName: String = "")

    suspend fun loadSession()
}
