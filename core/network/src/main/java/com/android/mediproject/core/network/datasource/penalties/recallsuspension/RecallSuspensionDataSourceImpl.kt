package com.android.mediproject.core.network.datasource.penalties.recallsuspension

import com.android.mediproject.core.model.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class RecallSuspensionDataSourceImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
) : RecallSuspensionDataSource {


    override fun getDetailRecallSuspensionInfo(
        company: String?, product: String?,
    ): Flow<Result<DetailRecallSuspensionResponse>> = channelFlow {
        send(dataGoKrNetworkApi.getDetailRecallSuspensionInfo(company = company, product = product).onDataGokrResponse())
    }

    override suspend fun getRecallSuspensionList(pageNo: Int, numOfRows: Int) =
        dataGoKrNetworkApi.getRecallSuspensionList(pageNo = pageNo, numOfRows = numOfRows).onDataGokrResponse()
}
