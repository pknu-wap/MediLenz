package com.android.mediproject.core.domain.sign

import android.util.Log
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.user.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject


class GetUserUseCase @Inject constructor(private val appDataStore: AppDataStore) {
    suspend operator fun invoke(): Flow<UserDto> = channelFlow {
        appDataStore.nickName.collect { nickName ->
            Log.d("wap",nickName)
            trySend(UserDto(nickName = nickName))
        }
    }
}