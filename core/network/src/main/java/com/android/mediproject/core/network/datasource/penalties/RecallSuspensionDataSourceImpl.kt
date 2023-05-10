package com.android.mediproject.core.network.datasource.penalties

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
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
            response.isSuccess().run {
                if (this == DataGoKrResult.isSuccess) response.body?.items?.firstOrNull()?.item?.let { item -> Result.success(item) }
                    ?: Result.failure(
                        Throwable(
                            "Response body is null"
                        )
                    )
                else Result.failure(Throwable(this.failedMessage))
            }
        }

    override suspend fun getRecallSuspensionList(pageNo: Int): Result<RecallSuspensionListResponse> =
        penaltiesNetworkApi.getRecallSuspensionList(pageNo = pageNo).let { response ->
            response.isSuccess().run {
                if (this == DataGoKrResult.isSuccess) response.body?.items?.let { Result.success(response) } ?: Result.failure(
                    Throwable(
                        "Response body is null"
                    )
                )
                else Result.failure(Throwable(this.failedMessage))
            }
        }
}