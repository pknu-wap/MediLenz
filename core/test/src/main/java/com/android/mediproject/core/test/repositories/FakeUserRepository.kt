package com.android.mediproject.core.test.repositories

import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.data.user.UserRepository
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParamter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.model.user.remote.ChangeNicknameResponse
import com.android.mediproject.core.model.user.remote.ChangePasswordResponse
import com.android.mediproject.core.model.user.remote.UserResponse
import com.android.mediproject.core.model.user.remote.WithdrawalResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class FakeUserInfoRepository : UserInfoRepository {
    private val _myAccountInfo = MutableStateFlow<AccountState>(AccountState.Unknown)
    override val myAccountInfo: StateFlow<AccountState>
        get() = _myAccountInfo.asStateFlow()

    override fun getMyAccountInfo(): Flow<Result<UserResponse>> = channelFlow{
    }

    override suspend fun updateMyAccountInfo(accountState: AccountState){

    }

    override suspend fun loadAccountState() {

    }
}
