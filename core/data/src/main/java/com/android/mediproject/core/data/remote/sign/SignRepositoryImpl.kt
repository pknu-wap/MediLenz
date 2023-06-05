package com.android.mediproject.core.data.remote.sign

import android.util.Log
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.datastore.TokenDataSource
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource, private val tokenDataSource: TokenDataSource, private val appDataStore: AppDataStore) :
    SignRepository {

    override val myEmail: Flow<String> = appDataStore.userEmail

    /**
     * 서버에 로그인 요청을 하고, 토큰 정보를 받는다.
     *
     * @param signInParameter 로그인 요청 파라미터
     * @return 응답받은 토큰
     */
    override fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>> = channelFlow {
        // 로그인 요청
        signDataSource.signIn(signInParameter).map { result ->
            result.fold(onSuccess = { dto ->
                // 이메일 저장
                if (signInParameter.isSavedEmail) appDataStore.saveUserEmail(signInParameter.email)
                else appDataStore.saveUserEmail(charArrayOf())
                appDataStore.saveSkipIntro(true)

                // 로그인 성공
                Result.success(Unit)
            }, onFailure = { throwable ->
                // 로그인 실패
                Result.failure(throwable)
            })
        }.collectLatest {
            trySend(it)
        }
    }


    /**
     * 서버에 회원가입 요청을 하고, 토큰 정보를 받는다.
     * @param signUpParameter 회원가입 요청 파라미터
     * @return 응답받은 토큰
     */
    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = channelFlow {
        // 회원가입 요청
        signDataSource.signUp(signUpParameter).map { result ->
            result.fold(onSuccess = { dto ->
                appDataStore.saveSkipIntro(true)
                // 회원가입 성공
                Result.success(Unit)
            }, onFailure = { throwable ->
                // 회원가입 실패
                Result.failure(throwable)
            })
        }.collectLatest {
            trySend(it)
        }
    }


    /**
     * 메모리상에 있는 현재 토큰의 상태에 따라 새로운 토큰을 서버에 요청한다.
     *
     * refresh token이 없는 경우, 토큰 재발급 불가
     *
     * return Result<Unit>.failure()
     *
     * refresh token이 있는 경우, 토큰 재발급 가능
     *
     * return  Result<Unit>.success()
     */
    private fun reissueToken(): Flow<Result<Unit>> = flow {
        when (val currentToken = tokenDataSource.currentTokens().last()) {
            is TokenState.AccessExpiration -> {
                // refresh token이 있는 상태이므로 토큰 재발급 요청
                Log.d("wap", "reissueToken: refresh token이 있는 상태이므로 토큰 재발급 요청")
                signDataSource.reissueTokens(currentToken.data.refreshToken).collectLatest {
                    emit(Result.success(Unit))
                }
            }

            is TokenState.RefreshExpiration -> {
                // 모든 토큰이 만료되었으므로 토큰 재발급 불가
                Log.d("wap", "reissueToken: 모든 token이 만료됨")
                emit(Result.failure(Exception("모든 token이 만료됨")))
            }

            is TokenState.Empty -> {
                // refresh token이 없는 상태이므로 토큰 재발급 불가
                Log.d("wap", "reissueToken: refresh token이 없는 상태이므로 토큰 재발급 불가")
                emit(Result.failure(Exception("refresh token이 없습니다")))
            }

            is TokenState.Valid -> {
                // 아직 토큰이 유효하므로 재발급 불필요
                Log.d("wap", "reissueToken: 아직 토큰이 유효하므로 재발급 불필요")
                emit(Result.success(Unit))
            }

            else -> {}
        }
    }

    /**
     * 로그아웃
     *
     * 저장된 토큰 정보를 삭제한다.
     */
    override suspend fun signOut() = tokenDataSource.removeTokens()


    /**
     * 현재 토큰의 상태를 반환한다.
     *
     * 외부에서 인터페이스로 접근할 때, 이 Flow를 collect하면, 토큰의 상태를 받을 수 있다.
     *
     * 만약 호출 했을 때, 만료되었으면 reissueToken()을 자동으로 호출해서 새로운 토큰을 반환한다.
     */
    override fun getCurrentTokens(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        tokenDataSource.currentTokens().collectLatest { tokenState ->
            when (tokenState) {
                is TokenState.AccessExpiration -> {
                    // access token이 만료되었으므로 토큰 재발급 요청
                    Log.d("wap", "getCurrentTokens: access token이 만료되었으므로 토큰 재발급 요청")
                    reissueToken().collectLatest {
                        val newState = it.fold(onSuccess = {
                            tokenDataSource.currentTokens().last()
                        }, onFailure = { TokenState.Error(Throwable("failed")) })
                        trySend(newState)
                    }
                }

                else -> {
                    // 1. 토큰이 없거나 유효한 경우 -> 그대로 반환(Error or Valid)
                    // 2.모든 토큰이 만료되었으므로 토큰 재발급 불가 -> 로그인 새로 ㄱㄱ
                    Log.d("wap", "getCurrentTokens: 토큰이 없거나 유효한 경우 -> 그대로 반환(Error or Valid)")
                    trySend(tokenState)
                }
            }
        }
    }
}