package com.android.mediproject.core.network.datasource.user

import android.util.Log
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.requestparameters.ChangePasswordRequestParameter
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.lang.ref.WeakReference
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(private val awsNetworkApi: AwsNetworkApi) :
    UserDataSource {

    /**
     * 닉네임 변경
     */
    override suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter): Flow<Result<ChangeNicknameResponse>> =
        channelFlow {
            awsNetworkApi.changeNickname(changeNicknameParameter).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
                .also { trySend(it) }
        }

    /**
     * 비밀번호 변경
     */
    override suspend fun changePassword(changePasswordParamter: ChangePasswordParamter): Flow<Result<ChangePasswordResponse>> =
        channelFlow {
            val password = changePasswordParamter.newPassword.hashCode().toString()
            awsNetworkApi.changePassword(ChangePasswordRequestParameter(password)).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
                .also { trySend(it) }
        }

    /**
     * 회원 탈퇴
     */
    override suspend fun withdrawal(): Flow<Result<WithdrawalResponse>> = channelFlow {
        Log.d("wap", "UserDataSource : withdrawal()")
        awsNetworkApi.withdrawal().onResponse()
            .fold(onSuccess = {
                Log.d("wap","dataSource : 성공")
                Result.success(it) }, onFailure = {
                Log.d("wap","dataSource : 실패 에러내용 : "+ it.toString())
                Result.failure(it) })
            .also { trySend(it) }
    }
}