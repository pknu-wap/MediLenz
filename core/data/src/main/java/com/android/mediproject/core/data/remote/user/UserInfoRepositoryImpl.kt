package com.android.mediproject.core.data.remote.user

import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource
) : UserInfoRepository {

    @Inject lateinit var signRepository: SignRepository

    private val _myAccountInfo = MutableStateFlow<AccountState>(AccountState.Unknown)

    override val myAccountInfo get() = _myAccountInfo as StateFlow<AccountState>

    override suspend fun updateMyAccountInfo(accountState: AccountState) {
        _myAccountInfo.value = accountState
    }

    /**
     * 로그인 상태 확인, 동시에 토큰 처리
     */
    override suspend fun loadAccountState() {
        getMyAccountInfo().zip(signRepository.getCurrentTokens()) { r1, r2 -> r1 to r2 }.collect { results ->
            val accountResult = results.first
            val tokenResult = results.second

            accountResult.onSuccess {
                if (tokenResult is TokenState.Valid) {
                    accountResult.onSuccess {
                        _myAccountInfo.value = AccountState.SignedIn(it.userId, it.nickname, it.email)
                    }
                }
            }.onFailure {
                _myAccountInfo.value = AccountState.Unknown
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