package com.android.mediproject.core.data.user

import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParameter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Flow<Result<ChangeNicknameResponse>>
    suspend fun changePassword(changePasswordParameter: ChangePasswordParameter): Flow<Result<ChangePasswordResponse>>
    suspend fun withdrawal(): Flow<Result<WithdrawalResponse>>
}
