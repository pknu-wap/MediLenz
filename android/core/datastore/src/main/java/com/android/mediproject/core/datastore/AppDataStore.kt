package com.android.mediproject.core.datastore

import kotlinx.coroutines.flow.Flow

interface AppDataStore {
    val userEmail: Flow<String>
    val nickName: Flow<String>
    val skipIntro: Flow<Boolean>
    val myAccountId: Flow<Long>
    suspend fun saveMyAccountInfo(userEmail: String, nickName: String, myAccountId: Long)
    suspend fun saveNickName(nickName: String)
    suspend fun saveSkipIntro(skipIntro: Boolean)
    suspend fun clearMyAccountInfo()
}