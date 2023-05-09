package com.android.mediproject.core.network.module

import com.android.mediproject.core.network.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [MedicineApprovalNetwork::class, PenaltiesNetwork::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val contentType = "application/json".toMediaType()


    /**
     * 요청 및 응답 정보를 기록하는 OkHttp 인터셉터를 추가한다.
     */
    @Provides
    @Singleton
    fun provideOkHttpCallFactory(): Call.Factory = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        },
    ).build()

    /**
     * Retrofit Builder를 제공한다.
     */
    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpCallFactory: Call.Factory): Retrofit.Builder =
        Retrofit.Builder().callFactory(okHttpCallFactory).addConverterFactory(
            Json.asConverterFactory("application/json".toMediaType()),
        )
}