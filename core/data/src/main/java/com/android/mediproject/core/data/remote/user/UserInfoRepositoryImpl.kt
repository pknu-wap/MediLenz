package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.model.user.UserDto
import com.android.mediproject.core.model.user.remote.toUserDto
import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource
) : UserInfoRepository {

    override var myAccountInfo: UserDto? = null

    /**
     * 서버에 내 계정 정보를 요청한다.
     *
     * 요청 받으면 이 레포지토리의 myAccountInfo에 저장한다.
     *
     * @return 응답받은 내 계정 정보
     *
     */
    override fun getMyAccountInfo() = channelFlow {
        userInfoDataSource.getUserInfo().collectLatest {
            it.onSuccess { userResponse ->
                myAccountInfo = userResponse.toUserDto()
            }.onFailure {
                myAccountInfo = null
            }
            trySend(it)
        }
    }

}