package com.android.mediproject.core.network.datasource.penalties.adminaction

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.toResult
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AdminActionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val dataGoKrNetworkApi: DataGoKrNetworkApi,
) : AdminActionDataSource {

    override suspend fun getAdminActionList(pageNo: Int) =
        dataGoKrNetworkApi.getAdminActionList(pageNo = pageNo).onResponse().fold(
            onSuccess = { response ->
                response.toResult()
            },
            onFailure = {
                Result.failure(it)
            },
        )

}
