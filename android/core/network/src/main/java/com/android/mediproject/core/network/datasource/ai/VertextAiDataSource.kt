package com.android.mediproject.core.network.datasource.ai

import com.android.mediproject.core.model.ai.VertexAiPredictResponse
import com.android.mediproject.core.model.requestparameters.VertexAiPredictParameter
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body

interface VertextAiDataSource {
    fun predict(@Body parameter: VertexAiPredictParameter): Flow<Result<VertexAiPredictResponse>>
}