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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource,
    private val tokenDataSource: TokenDataSource,
    private val appDataStore: AppDataStore
) : SignRepository {

    override val savedEmail: Flow<String> = appDataStore.userEmail

    /**
     * 서버에 로그인 요청을 하고, 토큰 정보를 받는다.
     *
     * @param signInParameter 로그인 요청 파라미터
     * @return 응답받은 토큰
     */
    override suspend fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>> =
        channelFlow {
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
                close()
            }
        }


    /**
     * 서버에 회원가입 요청을 하고, 토큰 정보를 받는다.
     * @param signUpParameter 회원가입 요청 파라미터
     * @return 응답받은 토큰
     */
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> =
        channelFlow {
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
                close()
            }
        }


    /**
     * 현재 토큰의 상태에 따라 새로운 토큰을 서버에 요청한다.
     */
    override suspend fun reissueToken(): Flow<Result<Unit>> = channelFlow {
        when (val tokenState = tokenDataSource.currentTokens().last()) {
            is TokenState.Empty -> send(Result.failure(Exception("Empty Token")))
            is TokenState.Expiration -> reissueToken(tokenState.data).collectLatest {
                send(it)
            }

            is TokenState.Valid -> send(Result.success(Unit))
            is TokenState.Error -> send(Result.failure(tokenState.throwable))
        }
    }


    /**
     * 서버에 새로운 토큰을 요청한다.
     */
    private suspend fun reissueToken(currentTokenDto: CurrentTokenDto): Flow<Result<Unit>> =
        channelFlow {
            signDataSource.reissueTokens(currentTokenDto.refreshToken).map { result ->
                result.fold(onSuccess = { dto ->
                    // 새로운 토큰 발급 성공
                    Result.success(Unit)
                }, onFailure = { throwable ->
                    // 새로운 토큰 발급 실패
                    Result.failure(throwable)
                })
            }.collectLatest {
                trySend(it)
                close()
            }
        }

    /**
     * 저장된 토큰 정보를 삭제한다.
     */
    override suspend fun signOut() = tokenDataSource.signOut()

    /**
     * 외부에서 인터페이스로 접근할 때, 이 Flow를 collect하면, 토큰의 상태를 받을 수 있다.
     * 만약 호출 했을 때, 만료되었으면 reissueToken()을 자동으로 호출해서 새로운 토큰을 반환한다.
     */
    override suspend fun getCurrentTokens(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        tokenDataSource.currentTokens().collectLatest { currentToken ->
            when (currentToken) {
                is TokenState.Empty -> trySend(TokenState.Empty)
                is TokenState.Error -> trySend(TokenState.Error(Throwable("Error")))
                is TokenState.Valid -> {
                    Log.d("wap", "SignRepositoryImpl" + currentToken.toString())
                    trySend(currentToken)
                }

                is TokenState.Expiration -> {
                    reissueToken(currentToken.data).collectLatest {
                        val newState = it.fold(onSuccess = { currentToken },
                            onFailure = { TokenState.Error(Throwable("failed")) })
                        trySend(newState)
                    }
                }
            }
        }
    }
}