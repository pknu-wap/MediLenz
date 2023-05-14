package com.android.mediproject.core.network.module

import com.android.mediproject.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module(includes = [MedicineApprovalNetwork::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {
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

}