package com.android.mediproject.core.network.datasource.penalties.recallsuspension

import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class RecallSaleSuspensionDataSourceImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
) : RecallSaleSuspensionDataSource {


    override fun getDetailRecallSaleSuspension(
        company: String?, product: String?,
    ): Flow<Result<DetailRecallSaleSuspensionResponse>> = channelFlow {
        send(dataGoKrNetworkApi.getDetailRecallSuspensionInfo(company = company, product = product).onDataGokrResponse())
    }

    override suspend fun getRecallSaleSuspensionList(pageNo: Int, numOfRows: Int) =
        dataGoKrNetworkApi.getRecallSuspensionList(pageNo = pageNo, numOfRows = numOfRows).onDataGokrResponse()
}
