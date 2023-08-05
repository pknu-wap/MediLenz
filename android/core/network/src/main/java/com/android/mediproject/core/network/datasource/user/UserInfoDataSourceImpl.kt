package com.android.mediproject.core.network.datasource.user

import com.android.mediproject.core.model.user.remote.UserResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserInfoDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi
) : UserInfoDataSource {
    override fun getUserInfo(): Flow<Result<UserResponse>> = flow {
        awsNetworkApi.getUserInfo().onResponse().fold(onSuccess = { response ->
            Result.success(response)
        }, onFailure = {
            Result.failure(it)
        }).also {
            emit(it)
        }
        
    }
}