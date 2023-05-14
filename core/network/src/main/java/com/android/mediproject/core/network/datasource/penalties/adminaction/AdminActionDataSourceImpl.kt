package com.android.mediproject.core.network.datasource.penalties.adminaction

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.network.module.PenaltiesNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AdminActionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val penaltiesNetworkApi: PenaltiesNetworkApi
) : AdminActionDataSource {

    override suspend fun getAdminActionList(pageNo: Int) =
        penaltiesNetworkApi.getAdminActionList(pageNo = pageNo).onResponse().fold(onSuccess = { response ->
            response.isSuccess().let {
                if (it == DataGoKrResult.isSuccess) Result.success(response)
                else Result.failure(Throwable(it.failedMessage))
            }
        }, onFailure = {
            Result.failure(it)
        })

}