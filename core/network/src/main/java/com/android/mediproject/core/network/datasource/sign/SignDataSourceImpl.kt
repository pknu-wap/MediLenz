package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.RequestBehavior
import com.android.mediproject.core.model.requestparameters.LoginParameter

import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.network.datasource.tokens.onResponseWithTokens
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import com.android.mediproject.core.network.tokens.TokenServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.lang.ref.WeakReference
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi, private val tokenServer: TokenServer, private val aesCoder: AesCoder,
) : SignDataSource {


    /**
     * 로그인
     */
    override fun logIn(loginParameter: LoginParameter): Flow<Result<SignInResponse>> = channelFlow {
        awsNetworkApi.login(
            SignInRequestParameter(
                WeakReference(loginParameter.email.joinToString("")).get()!!,
                WeakReference(aesCoder.encodePassword(loginParameter.email, loginParameter.password)).get()!!,
            ),
        ).onResponseWithTokens(RequestBehavior.NewTokens, tokenServer).fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also {

            trySend(it)
        }
    }

    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>> = channelFlow {
        awsNetworkApi.signUp(
            SignUpRequestParameter(
                WeakReference(signUpParameter.email.joinToString("")).get()!!,
                WeakReference(aesCoder.encodePassword(signUpParameter.email, signUpParameter.password)).get()!!, signUpParameter.nickName,
            ),
        ).onResponseWithTokens(RequestBehavior.NewTokens, tokenServer).fold(
            onSuccess = {
                Result.success(it)

            },
            onFailure = { Result.failure(it) },
        ).also {
            trySend(it)
        }
    }

}
