package com.android.mediproject.core.test.repositories

import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow

class FakeUserInfoRepository : UserInfoRepository {
    private val _myAccountInfo = MutableStateFlow<AccountState>(AccountState.Unknown)
    override val myAccountInfo: StateFlow<AccountState>
        get() = _myAccountInfo.asStateFlow()

    override fun getMyAccountInfo(): Flow<Result<User>> = channelFlow{
    }

    override suspend fun updateMyAccountInfo(accountState: AccountState){

    }

    override suspend fun loadAccountState() {

    }
}
