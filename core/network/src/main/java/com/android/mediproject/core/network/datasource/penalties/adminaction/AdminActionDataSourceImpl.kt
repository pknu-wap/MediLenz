package com.android.mediproject.core.network.datasource.penalties.adminaction

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import com.android.mediproject.core.network.module.PenaltiesNetworkApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AdminActionDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val penaltiesNetworkApi: PenaltiesNetworkApi
) : AdminActionDataSource {

    override suspend fun getAdminActionList(pageNo: Int): Result<AdminActionListResponse> =
        penaltiesNetworkApi.getAdminActionList(pageNo = pageNo).let { response ->
            response.isSuccess().run {
                if (this == DataGoKrResult.isSuccess) response.body?.let { items -> Result.success(response) }
                    ?: Result.failure(
                        Throwable(
                            "Response body is null"
                        )
                    )
                else Result.failure(Throwable(this.failedMessage))
            }
        }
}