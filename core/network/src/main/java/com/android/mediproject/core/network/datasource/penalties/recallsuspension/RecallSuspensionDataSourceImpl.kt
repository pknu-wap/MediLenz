package com.android.mediproject.core.network.datasource.penalties.recallsuspension

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.network.module.PenaltiesNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RecallSuspensionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val penaltiesNetworkApi: PenaltiesNetworkApi
) : RecallSuspensionDataSource {


    override suspend fun getDetailRecallSuspensionInfo(
        pageNo: Int, company: String?, product: String?
    ) = penaltiesNetworkApi.getDetailRecallSuspensionInfo(pageNo = pageNo, company = company, product = product).onResponse()
        .fold(onSuccess = { response ->
            response.isSuccess().let {
                if (it == DataGoKrResult.isSuccess) Result.success(response)
                else Result.failure(Throwable(it.failedMessage))
            }
        }, onFailure = {
            Result.failure(it)
        })


    override suspend fun getRecallSuspensionList(pageNo: Int) =
        penaltiesNetworkApi.getRecallSuspensionList(pageNo = pageNo).onResponse().fold(onSuccess = { response ->
            response.isSuccess().let {
                if (it == DataGoKrResult.isSuccess) Result.success(response)
                else Result.failure(Throwable(it.failedMessage))
            }
        }, onFailure = {
            Result.failure(it)
        })

}