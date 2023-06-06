package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import com.android.mediproject.core.network.datasource.user.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Flow<Result<ChangeNicknameResponse>> =
        channelFlow {
            userDataSource.changeNickname(changeNicknameParameter).map {
                it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
            }.collectLatest { trySend(it) }
        }

    override suspend fun changePassword(changePasswordParamter: ChangePasswordParamter): Flow<Result<ChangePasswordResponse>> =
        channelFlow {
            userDataSource.changePassword(changePasswordParamter).map {
                it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
            }.collectLatest { trySend(it) }
        }

    override suspend fun withdrawal(): Flow<Result<WithdrawalResponse>> = channelFlow {
        userDataSource.withdrawal().map {
            it.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }
}