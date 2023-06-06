package com.android.mediproject.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context) : AppDataStore {
    private val Context.dataStore by preferencesDataStore("user_preferences")

    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_NICK_NAME = stringPreferencesKey("nick_name")
    private val KEY_SKIP_INTRO = booleanPreferencesKey("skip_intro")
    private val KEY_MY_ACCOUNT_ID = longPreferencesKey("my_account_id")


    override val userEmail: Flow<String> = context.dataStore.data.map { it[KEY_USER_EMAIL] ?: "" }

    override val nickName: Flow<String> = context.dataStore.data.map { it[KEY_NICK_NAME] ?: "" }

    override val skipIntro: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_SKIP_INTRO] ?: false
    }

    override val myAccountId: Flow<Long> = context.dataStore.data.map {
        it[KEY_MY_ACCOUNT_ID] ?: 0L
    }


    override suspend fun saveUserEmail(email: CharArray) {
        context.dataStore.edit { it[KEY_USER_EMAIL] = email.joinToString("") }
    }

    override suspend fun saveNickName(nickName: String) {
        context.dataStore.edit { it[KEY_NICK_NAME] = nickName }
    }

    override suspend fun saveSkipIntro(skipIntro: Boolean) {
        context.dataStore.edit { it[KEY_SKIP_INTRO] = skipIntro }
    }

    override suspend fun saveMyAccountId(myAccountId: Long) {
        context.dataStore.edit { it[KEY_MY_ACCOUNT_ID] = myAccountId }
    }
}