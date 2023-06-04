package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.datastore.TokenDataSourceImpl
import com.android.mediproject.core.model.remote.medicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.ReissueTokenResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.datasource.sign.SignDataSourceImpl
import com.android.mediproject.core.network.parameter.SignInRequestParameter
import com.android.mediproject.core.network.parameter.SignUpRequestParameter
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AwsNetwork {

    @Provides()
    @Named("awsNetworkApiWithAccessTokens")
    @Singleton
    fun providesAwsNetworkApi(
        @Named("okHttpClientWithAccessTokens") okHttpClient: OkHttpClient,
    ): AwsNetworkApi = Retrofit.Builder().client(okHttpClient).addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    ).baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Named("awsNetworkApiWithRefreshTokens")
    @Singleton
    fun providesReissueTokenAwsNetworkApi(
        @Named("okHttpClientWithReissueTokens") okHttpClient: OkHttpClient,
    ): AwsNetworkApi = Retrofit.Builder().client(okHttpClient).addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    ).baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesCommentsDataSource(@Named("awsNetworkApiWithAccessTokens") awsNetworkApi: AwsNetworkApi): CommentsDataSource =
        CommentsDataSourceImpl(awsNetworkApi)

    @Provides
    @Singleton
    fun providesSignDataSource(
        @Named("awsNetworkApiWithRefreshTokens") awsNetworkApi: AwsNetworkApi,
        tokenDataSourceImpl: TokenDataSourceImpl,
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): SignDataSource = SignDataSourceImpl(
        awsNetworkApi, tokenDataSourceImpl, ioDispatcher
    )
}

interface AwsNetworkApi {
    @POST(value = "user/register")
    suspend fun signUp(
        @Body signUpRequestParameter: SignUpRequestParameter
    ): Response<SignUpResponse>

    @POST(value = "user/login")
    suspend fun signIn(
        @Body signInRequestParameter: SignInRequestParameter
    ): Response<SignInResponse>


    @POST(value = "user/reissue")
    suspend fun reissueTokens(
    ): Response<ReissueTokenResponse>

    @GET(value = "medicine/favorite")
    suspend fun getInterestedMedicineList() : Response<InterestedMedicineListResponse>
}