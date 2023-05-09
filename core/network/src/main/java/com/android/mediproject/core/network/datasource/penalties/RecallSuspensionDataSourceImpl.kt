package com.android.mediproject.core.network.datasource.penalties

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import com.android.mediproject.core.network.module.PenaltiesNetworkApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RecallSuspensionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val penaltiesNetworkApi: PenaltiesNetworkApi
) : RecallSuspensionDataSource {


    override suspend fun getDetailRecallSuspensionInfo(
        pageNo: Int, company: String?, product: String?
    ): Result<DetailRecallSuspensionResponse.Body.Item.Item> =
        penaltiesNetworkApi.getDetailRecallSuspensionInfo(pageNo = pageNo, company = company, product = product).let { response ->
            if (response.isSuccess()) {
                response.body?.items?.firstOrNull()?.item?.let { Result.success(it) }
                    ?: Result.failure(Throwable(response.header?.resultMsg))
            } else {
                Result.failure(Throwable(response.header?.resultMsg))
            }
        }

    override suspend fun getRecallSuspensionList(pageNo: Int): RecallSuspensionListResponse =
        penaltiesNetworkApi.getRecallSuspensionList(pageNo = pageNo)
}