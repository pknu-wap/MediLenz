package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.model.remote.sign.SignInResponse
import com.android.mediproject.core.model.remote.sign.SignUpResponse
import com.android.mediproject.core.model.remote.token.AccessTokenResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.datasource.sign.SignDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AwsNetwork {
    @Provides
    @Singleton
    fun providesAwsNetworkApi(okHttpClient: OkHttpClient): AwsNetworkApi = Retrofit.Builder().client(okHttpClient).addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    ).baseUrl(BuildConfig.AWS_BASE_URL).build().create(AwsNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesCommentsDataSource(awsNetworkApi: AwsNetworkApi): CommentsDataSource = CommentsDataSourceImpl(awsNetworkApi)

    @Provides
    @Singleton
    fun providesSignDataSource(awsNetworkApi: AwsNetworkApi): SignDataSource = SignDataSourceImpl(awsNetworkApi)
}

interface AwsNetworkApi {
    @FormUrlEncoded
    @POST(value = "user/register")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String, @Field("email") nickname: String,
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST(value = "user/login")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<SignInResponse>


    @FormUrlEncoded
    @POST(value = "user/reissue")
    suspend fun reissueTokens(
        @Header("authorization") refreshToken: String,
    ): Response<AccessTokenResponse>
}