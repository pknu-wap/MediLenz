package com.android.mediproject.core.network.module

import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSource
import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSourceImpl
import com.android.mediproject.core.network.parser.HtmlParser
import com.android.mediproject.core.network.retrofitext.NetworkApiCallAdapterFactory
import com.android.mediproject.core.network.retrofitext.NetworkApiResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleSearchNetwork {

    private const val BASE_URL = "https://www.google.com"

    @Provides
    @Singleton
    fun providesGoogleSearchNetworkApi(@Named("okHttpClientWithoutAny") okHttpClient: OkHttpClient): GoogleSearchNetworkApi =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(NetworkApiCallAdapterFactory())
            .baseUrl(BASE_URL).build()
            .create(GoogleSearchNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideGoogleSearchDataSource(
        googleSearchNetworkApi: GoogleSearchNetworkApi, htmlParser: HtmlParser,
    ): GoogleSearchDataSource = GoogleSearchDataSourceImpl(googleSearchNetworkApi, htmlParser)

}

interface GoogleSearchNetworkApi {
    @GET(value = "search")
    suspend fun getImageUrl(
        @Query("q", encoded = true) query: String,
        @Query("tbm", encoded = true) tbm: String = ISCH,
        @Query("ie", encoded = true) ie: String = ENCODING,
        @Query("oe", encoded = true) oe: String = ENCODING,
    ): NetworkApiResult<String>
}

private const val ENCODING: String = "utf8"
private const val ISCH: String = "isch"
