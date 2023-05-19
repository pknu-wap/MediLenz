package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.remote.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MedicineApprovalDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val dataGoKrNetworkApi: DataGoKrNetworkApi
) : MedicineApprovalDataSource {


    override suspend fun getMedicineApprovalList(
        itemName: String?, entpName: String?, medicationType: String?, pageNo: Int
    ): Result<MedicineApprovalListResponse> = dataGoKrNetworkApi.getApprovalList(
        itemName = itemName, entpName = entpName, pageNo = pageNo, medicationType = medicationType
    ).onResponse().fold(onSuccess = { response ->
        response.isSuccess().let {
            if (it == DataGoKrResult.isSuccess) Result.success(response)
            else Result.failure(Throwable(it.failedMessage))
        }
    }, onFailure = {
        Result.failure(it)
    })

    override suspend fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse>> = flow {
        dataGoKrNetworkApi.getMedicineDetailInfo(itemName = itemName).onResponse().fold(onSuccess = { response ->
            response.isSuccess().let {
                if (it == DataGoKrResult.isSuccess) Result.success(response)
                else Result.failure(Throwable(it.failedMessage))
            }.also {
                emit(it)
            }
        }, onFailure = {
            emit(Result.failure(it))
        })

    }
}