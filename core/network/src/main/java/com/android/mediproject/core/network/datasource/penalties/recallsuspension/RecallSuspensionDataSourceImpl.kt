package com.android.mediproject.core.network.datasource.penalties.recallsuspension

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecallSuspensionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val dataGoKrNetworkApi: DataGoKrNetworkApi
) : RecallSuspensionDataSource {


    override fun getDetailRecallSuspensionInfo(
        company: String?, product: String?
    ): Flow<Result<DetailRecallSuspensionResponse>> = flow {
        dataGoKrNetworkApi.getDetailRecallSuspensionInfo(company = company, product = product)
            .onResponse().fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it == DataGoKrResult.isSuccess) Result.success(response)
                    else Result.failure(Throwable(it.failedMessage))
                }
            }, onFailure = {
                Result.failure(it)
            }).also {
                emit(it)
            }
    }

    override suspend fun getRecallSuspensionList(pageNo: Int, numOfRows: Int) =
        dataGoKrNetworkApi.getRecallSuspensionList(pageNo = pageNo, numOfRows = numOfRows)
            .onResponse().fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it == DataGoKrResult.isSuccess) Result.success(response)
                    else Result.failure(Throwable(it.failedMessage))
                }
            }, onFailure = {
                Result.failure(it)
            })

}