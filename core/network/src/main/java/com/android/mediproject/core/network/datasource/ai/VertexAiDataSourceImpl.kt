package com.android.mediproject.core.network.datasource.ai

import com.android.mediproject.core.model.ai.VertexAiPredictResponse
import com.android.mediproject.core.model.requestparameters.VertexAiPredictParameter
import com.android.mediproject.core.network.module.GoogleNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VertexAiDataSourceImpl @Inject constructor(
    private val googleNetworkApi: GoogleNetworkApi) : VertextAiDataSource {

    override fun predict(parameter: VertexAiPredictParameter): Flow<Result<VertexAiPredictResponse>> = flow {
        googleNetworkApi.predict(parameter = parameter).onResponse().onSuccess {
            emit(Result.success(it))
        }.onFailure {
            emit(Result.failure(it))
        }
    }
}