package com.android.mediproject.core.data.user

import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserInfoRepository {
    val myAccountInfo: StateFlow<AccountState>
    fun getMyAccountInfo(): Flow<Result<User>>

    suspend fun updateMyAccountInfo(accountState: AccountState)

    suspend fun loadAccountState()
}
