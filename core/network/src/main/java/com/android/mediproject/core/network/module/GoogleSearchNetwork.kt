package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.network.datasource.image.GoogleImageDownloadDataSourceImpl
import com.android.mediproject.core.network.parser.HtmlParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleSearchNetwork {

    private const val BASE_URL = "https://www.google.com"

    @Provides
    @Singleton
    fun providesGoogleSearchNetworkApi(okHttpClient: OkHttpClient): GoogleSearchNetworkApi =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create()).baseUrl(BASE_URL).build()
            .create(GoogleSearchNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideGoogleImageDownloadDataSource(
        googleSearchNetworkApi: GoogleSearchNetworkApi, htmlParser: HtmlParser,
        @Dispatcher(MediDispatchers.Default) defaultDispatcher: CoroutineDispatcher,
    ) = GoogleImageDownloadDataSourceImpl(googleSearchNetworkApi, htmlParser, defaultDispatcher)

}


interface GoogleSearchNetworkApi {

    @GET(value = "search")
    suspend fun getImageUrl(@Query("q", encoded = true) query: String, @Query("tbm") tbm: String = "isch"): Response<String>
}
