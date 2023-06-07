package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource
) : UserInfoRepository {

    override fun getMyAccountInfo() = userInfoDataSource.getUserInfo()
    
}