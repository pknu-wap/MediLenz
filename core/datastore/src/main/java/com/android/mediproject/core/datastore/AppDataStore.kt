package com.android.mediproject.core.datastore

import kotlinx.coroutines.flow.Flow

interface AppDataStore {
    val userEmail: Flow<String>
    val nickName: Flow<String>
    val skipIntro: Flow<Boolean>
    val myAccountId: Flow<Long>
    suspend fun saveUserEmail(email: CharArray)
    suspend fun saveNickName(nickName: String)
    suspend fun saveSkipIntro(skipIntro: Boolean)
    suspend fun saveMyAccountId(myAccountId: Long)
}