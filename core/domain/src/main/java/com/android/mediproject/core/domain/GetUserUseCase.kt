package com.android.mediproject.core.domain

import android.util.Log
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject


class GetUserUseCase @Inject constructor(private val appDataStore: AppDataStore) {
    suspend operator fun invoke(): Flow<User> = channelFlow {
        appDataStore.nickName.collect { nickName ->
            Log.d("wap", nickName)
            trySend(User(nickName = nickName))
        }
    }
}