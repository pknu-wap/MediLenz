package com.android.mediproject.core.network.module

import com.android.mediproject.core.model.ai.VertexAiPredictResponse
import com.android.mediproject.core.model.requestparameters.VertexAiPredictParameter
import com.android.mediproject.core.network.datasource.ai.VertexAiDataSourceImpl
import com.android.mediproject.core.network.datasource.ai.VertextAiDataSource
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
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleNetwork {

    @Provides
    @Singleton
    fun providesGoogleNetworkApi(okHttpClient: OkHttpClient): GoogleNetworkApi =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(GOOGLE_ENDPOINT_URL).build().create(GoogleNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesVertexAiDataSource(
        googleNetworkApi: GoogleNetworkApi): VertextAiDataSource = VertexAiDataSourceImpl(googleNetworkApi)
}

interface GoogleNetworkApi {
    @POST(value = GOOGLE_ENDPOINT_URL)
    suspend fun predict(
        @Path("{PROJECT_ID}") projectId: String = "",
        @Path("{ENDPOINT_ID}") endpointId: String = "",
        @Body parameter: VertexAiPredictParameter): Response<VertexAiPredictResponse>
}

internal const val GOOGLE_ENDPOINT_URL =
    "https://us-central1-aiplatform.googleapis.com/v1/projects/{PROJECT_ID}/locations/us-central1/endpoints/{ENDPOINT_ID}:predict"