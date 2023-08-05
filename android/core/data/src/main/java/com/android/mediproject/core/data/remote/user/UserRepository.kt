package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Flow<Result<ChangeNicknameResponse>>
    suspend fun changePassword(changePasswordParamter: ChangePasswordParamter): Flow<Result<ChangePasswordResponse>>
    suspend fun withdrawal(): Flow<Result<WithdrawalResponse>>
}