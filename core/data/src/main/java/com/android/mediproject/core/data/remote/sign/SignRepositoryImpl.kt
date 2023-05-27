package com.android.mediproject.core.data.remote.sign

import com.android.mediproject.core.datastore.TokenDataSource
import com.android.mediproject.core.model.parameters.SignInParameter
import com.android.mediproject.core.model.parameters.SignUpParameter
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.tokens.TokenServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val tokenServer: TokenServer, private val signDataSource: SignDataSource, private val tokenDataSource: TokenDataSource
) : SignRepository {

    private val tokens: TokenState<CurrentTokenDto> = tokenServer.let {
        if (it.isTokenEmpty()) TokenState.Empty
        else if (it.isExpiredToken()) TokenState.Expiration
        else {
            val token = it.getTokens()
            TokenState.Valid(
                CurrentTokenDto(
                    token.accessToken.copyOf(),
                    token.refreshToken.copyOf(),
                    token.email.copyOf(),
                    token.expirationDateTime,
                )
            )
        }
    }

    /**
     * 서버에 로그인 요청을 하고, 토큰 정보를 받는다.
     *
     * @param signInParameter 로그인 요청 파라미터
     * @return 응답받은 토큰
     */
    override suspend fun signIn(signInParameter: SignInParameter): Flow<Result<Unit>> = channelFlow {
        // 로그인 요청
        signDataSource.signIn(signInParameter).map { result ->
            result.fold(onSuccess = { dto ->
                // 토큰 저장
                tokenDataSource.updateTokens(dto)
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
    override suspend fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = channelFlow {
        // 회원가입 요청
        signDataSource.signUp(signUpParameter).map { result ->
            result.fold(onSuccess = { dto ->
                // 토큰 저장
                tokenDataSource.updateTokens(dto)
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
     * 현재 토큰의 상태에 따라 새로운 토큰을 서버에 요청한다.
     */
    override suspend fun reissueToken(): Flow<Result<Unit>> = tokens.let { tokenState ->
        if (tokenState is TokenState.Valid) reissueToken(tokenState.data)
        else flowOf(Result.failure(Exception("Token is expired")))
    }


    /**
     * 서버에 새로운 토큰을 요청한다.
     */
    private suspend fun reissueToken(currentTokenDto: CurrentTokenDto): Flow<Result<Unit>> = channelFlow {
        signDataSource.reissueTokens(currentTokenDto.refreshToken, currentTokenDto.userEmail).map { result ->
            result.fold(onSuccess = { dto ->
                // 새로운 토큰 저장
                tokenDataSource.updateTokens(dto)
                // 새로운 토큰 발급 성공
                Result.success(Unit)
            }, onFailure = { throwable ->
                // 새로운 토큰 발급 실패
                Result.failure(throwable)
            })
        }.collectLatest {
            trySend(it)
        }
    }

    /**
     * 저장된 토큰 정보를 삭제한다.
     */
    override suspend fun signOut() {
        tokenDataSource.clearSavedTokens()
        tokenServer.isExpiredToken()
    }


    /**
     * 외부에서 인터페이스로 접근할 때, 이 Flow를 collect하면, 토큰의 상태를 받을 수 있다.
     */
    override suspend fun getCurrentTokens(): Flow<TokenState<CurrentTokenDto>> = flowOf(tokens)
}