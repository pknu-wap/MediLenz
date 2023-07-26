package com.android.mediproject.core.domain.sign

import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.model.user.AccountState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountStateUseCase @Inject constructor(private val userInfoRepository: UserInfoRepository) {
    operator fun invoke(): Flow<AccountState> = channelFlow {
        userInfoRepository.myAccountInfo.collectLatest {
            trySend(it)
        }
    }

    suspend fun loadAccountState() = userInfoRepository.loadAccountState()
}
