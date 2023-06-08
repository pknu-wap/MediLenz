package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class MedicineApprovalDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher, private val dataGoKrNetworkApi: DataGoKrNetworkApi
) : MedicineApprovalDataSource {

    override suspend fun getMedicineApprovalList(
        itemName: String?, entpName: String?, medicationType: String?, pageNo: Int
    ): Result<MedicineApprovalListResponse> =
        dataGoKrNetworkApi.getApprovalList(itemName = itemName, entpName = entpName, pageNo = pageNo, medicationType = medicationType)
            .onResponse().fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it == DataGoKrResult.isSuccess) Result.success(response)
                    else Result.failure(Throwable(it.failedMessage))
                }
            }, onFailure = {
                Result.failure(it)
            })

    override fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse>> = channelFlow {
        dataGoKrNetworkApi.getMedicineDetailInfo(itemName = itemName).onResponse().fold(onSuccess = { response ->
            response.isSuccess().let {
                if (it is DataGoKrResult.isSuccess) Result.success(response)
                else Result.failure(Throwable(it.failedMessage))
            }
        }, onFailure = {
            Result.failure<MedicineDetailInfoResponse>(it)
        }).also {
            send(it)
        }
    }

    override fun getMedicineDetailInfoByItemSeq(itemSeqs: List<String>) = channelFlow {
        val responses = itemSeqs.map { itemSeq ->
            dataGoKrNetworkApi.getMedicineDetailInfo(itemSeq = itemSeq).onResponse().fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it is DataGoKrResult.isSuccess) Result.success(response)
                    else Result.failure(Throwable(it.failedMessage))
                }
            }, onFailure = {
                Result.failure<MedicineDetailInfoResponse>(it)
            })
        }

        val results = responses.let {
            val failed = it.any { result -> result.isFailure }
            if (failed) Result.failure(Throwable("약품 상세 정보 조회에 실패했습니다."))
            else Result.success(it.map { result -> result.getOrNull()!! })
        }

        trySend(results)
    }
}