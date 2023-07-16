package com.android.mediproject.core.data.remote.sign

import com.android.mediproject.core.data.remote.user.UserInfoRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.requestparameters.SignInParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource,
    private val appDataStore: AppDataStore,
    private val userInfoRepository: UserInfoRepository,
) : SignRepository {

    /**
     * 서버에 로그인 요청을 하고, 토큰 정보를 받는다.
     *
     * @param signInParameter 로그인 요청 파라미터
     * @return 응답받은 토큰
     */
    override fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>> = channelFlow {
        signDataSource.signIn(signInParameter).collect { signInResult ->
            // 로그인 실패 처리
            if (signInResult.isFailure) {
                // 로그인 실패
                trySend(Result.failure(signInResult.exceptionOrNull() ?: Exception("로그인 실패")))
                return@collect
            }

            // 로그인 성공
            // 이메일 저장, 인트로 스킵, ID 저장
            appDataStore.apply {
                saveSkipIntro(true)
                signInResult.onSuccess {
                    // 내 계정 정보 메모리에 저장
                    userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                    saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                }
            }

            // 로그인 성공
            trySend(Result.success(Unit))
        }
    }


    /**
     * 서버에 회원가입 요청을 하고, 토큰 정보를 받는다.
     * @param signUpParameter 회원가입 요청 파라미터
     * @return 응답받은 토큰
     */
    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = channelFlow {
        // 회원가입 요청
        signDataSource.signUp(signUpParameter).collect { signUpResult ->

            // 두개 모두 성공하지 않은 경우에는 가입 실패 처리
            if (signUpResult.isFailure) {
                // 가입 실패
                trySend(Result.failure(signUpResult.exceptionOrNull() ?: Exception("로그인 실패")))
                return@collect
            }

            // 가입 성공
            // 인트로 스킵
            appDataStore.apply {
                saveSkipIntro(true)
                signUpResult.onSuccess {
                    // 내 계정 정보 메모리에 저장
                    userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                    saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                }
            }

            // 가입 성공
            trySend(Result.success(Unit))
        }
    }

    /**
     * 로그아웃
     *
     * 저장된 토큰 정보를 삭제한다.
     */
    override suspend fun signOut() = TODO()
}
