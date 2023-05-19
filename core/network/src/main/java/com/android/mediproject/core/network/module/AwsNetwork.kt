package com.android.mediproject.core.network.module

import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AwsNetwork {
    @Provides
    @Singleton
    fun providesAwsNetworkApi(okHttpClient: OkHttpClient): AwsNetworkApi = Retrofit.Builder().client(okHttpClient).addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    ).baseUrl("").build().create(AwsNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesCommentsDataSource(awsNetworkApi: AwsNetworkApi): CommentsDataSource = CommentsDataSourceImpl(awsNetworkApi)
}

interface AwsNetworkApi {

}