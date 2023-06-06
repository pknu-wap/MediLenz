package com.android.mediproject.core.network.datasource.user

import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    /**
     * 닉네임 변경
     */
    suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Flow<Result<ChangeNicknameResponse>>

    /**
     * 비밀번호 변경
     */
    suspend fun changePassword(changePasswordParamter: ChangePasswordParamter): Flow<Result<ChangePasswordResponse>>

    /**
     * 회원 탈퇴
     */
    suspend fun withdrawal(): Flow<Result<WithdrawalResponse>>
}