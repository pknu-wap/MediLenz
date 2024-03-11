package com.android.mediproject.core.data.user

import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParameter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import com.android.mediproject.core.network.datasource.user.UserDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override suspend fun withdrawal(): Result<WithdrawalResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Result<ChangeNicknameResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(changePasswordParameter: ChangePasswordParameter): Result<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }
}
