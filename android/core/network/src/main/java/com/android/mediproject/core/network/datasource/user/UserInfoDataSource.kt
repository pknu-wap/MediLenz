package com.android.mediproject.core.network.datasource.user

import com.android.mediproject.core.model.user.remote.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserInfoDataSource {

    fun getUserInfo(): Flow<Result<UserResponse>>
}