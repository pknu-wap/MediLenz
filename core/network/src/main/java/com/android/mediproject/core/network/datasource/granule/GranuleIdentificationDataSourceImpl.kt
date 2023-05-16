package com.android.mediproject.core.network.datasource.granule

import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import javax.inject.Inject

class GranuleIdentificationDataSourceImpl : GranuleIdentificationDataSource {

    @Inject private lateinit var networkApi: DataGoKrNetworkApi

    override suspend fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Result<GranuleIdentificationInfoResponse> =
        networkApi.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).onResponse()
            .fold(onSuccess = { response ->
                response.isSuccess().let {
                    if (it == DataGoKrResult.isSuccess) Result.success(response)
                    else Result.failure(Throwable(it.failedMessage))
                }
            }, onFailure = {
                Result.failure(it)
            })
}