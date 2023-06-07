package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.model.user.remote.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getMyAccountInfo(): Flow<Result<UserResponse>>
}