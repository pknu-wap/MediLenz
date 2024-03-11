package com.android.mediproject.core.domain

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
