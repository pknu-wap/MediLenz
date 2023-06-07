package com.android.mediproject.core.domain.user

import android.util.Log
import com.android.mediproject.core.data.remote.user.UserInfoRepository
import com.android.mediproject.core.data.remote.user.UserRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.model.user.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserUseCase @Inject constructor(
    private val appDataStore: AppDataStore,
    private val userRepository: UserRepository,
    private val getUserInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(): Flow<UserDto> = channelFlow {
        appDataStore.nickName.collect { nickName ->
            Log.d("wap", nickName)
            trySend(UserDto(nickName = nickName))
        }
    }

    suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter) = channelFlow {
        userRepository.changeNickname(changeNicknameParameter).map {
            it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    suspend fun changePassword(changePasswordParamter: ChangePasswordParamter) = channelFlow {
        val email = (getUserInfoRepository.myAccountInfo.value as AccountState.SignedIn).email.toCharArray()
        userRepository.changePassword(changePasswordParamter.apply {
            this.email = email
        }).map {
            it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    suspend fun withdrawal() = channelFlow {
        Log.d("wap", "UserUseCase : withdrawal()")
        userRepository.withdrawal().map {
            Log.d("wap", "UserUseCase : withdrawal()" + it.toString())
            it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }
}