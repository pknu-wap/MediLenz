package com.android.mediproject.core.data.user

import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource, private val appDataStore: AppDataStore,
) : UserInfoRepository {

    private val _myAccountInfo = MutableStateFlow<AccountState>(AccountState.Unknown)

    override val myAccountInfo get() = _myAccountInfo as StateFlow<AccountState>

    override suspend fun updateMyAccountInfo(accountState: AccountState) {
        _myAccountInfo.value = accountState
    }

    /**
     * 로그인 상태 확인, 동시에 토큰 처리
     */
    override suspend fun loadAccountState() {
        appDataStore.apply {
            myAccountId.zip(userEmail) { id, email ->
                id to email
            }.zip(nickName) { (id, email), nickName ->
                Triple(id, email, nickName)
            }.collectLatest {
                val id = it.first
                val email = it.second
                val nickName = it.third

                if (id != 0L && email.isNotEmpty() && nickName.isNotEmpty()) {
                    _myAccountInfo.value = AccountState.SignedIn(id, nickName, email)
                } else {
                    _myAccountInfo.value = AccountState.Unknown
                }
            }
        }
    }

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
                _myAccountInfo.value = AccountState.SignedIn(userResponse.userId, userResponse.nickname, userResponse.email)
            }.onFailure {
                _myAccountInfo.value = AccountState.Unknown
            }
            trySend(it)
        }
    }

}
