package com.android.mediproject.core.data.remote.ai

import com.android.mediproject.core.model.ai.VertexAiPredictResponse
import com.android.mediproject.core.model.requestparameters.VertexAiPredictParameter
import com.android.mediproject.core.network.datasource.ai.VertextAiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class VertexAiRepositoryImpl @Inject constructor(
    private val vertextAiDataSource: VertextAiDataSource) : VertexAiRepository {
    override fun predict(parameter: VertexAiPredictParameter): Flow<Result<VertexAiPredictResponse>> = channelFlow {
        vertextAiDataSource.predict(parameter).collect {
            send(it)
        }
    }
}