package com.android.mediproject.core.network.datasource.granule

import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GranuleIdentificationDataSourceImpl @Inject constructor(private val networkApi: DataGoKrNetworkApi) :
    GranuleIdentificationDataSource {

    override fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfoResponse>> = flow {
        networkApi.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).onResponse()
            .fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it is DataGoKrResult.isSuccess && response.body != null) {
                        emit(Result.success(response))
                    } else {
                        emit(Result.failure(Throwable(it.failedMessage)))
                    }
                }
            }, onFailure = {
                emit(Result.failure(it))
            })
    }
}