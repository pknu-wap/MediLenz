package com.android.mediproject.core.data.remote.ai

import com.android.mediproject.core.model.ai.VertexAiPredictResponse
import com.android.mediproject.core.model.requestparameters.VertexAiPredictParameter
import kotlinx.coroutines.flow.Flow

interface VertexAiRepository {
    fun predict(parameter: VertexAiPredictParameter): Flow<Result<VertexAiPredictResponse>>
}