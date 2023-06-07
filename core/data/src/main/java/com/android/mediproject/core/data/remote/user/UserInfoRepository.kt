package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.model.user.UserDto
import com.android.mediproject.core.model.user.remote.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    var myAccountInfo: UserDto?
    fun getMyAccountInfo(): Flow<Result<UserResponse>>
}