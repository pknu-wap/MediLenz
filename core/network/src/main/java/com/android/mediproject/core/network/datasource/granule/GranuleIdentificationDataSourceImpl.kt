package com.android.mediproject.core.network.datasource.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import com.android.mediproject.core.model.toResult
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GranuleIdentificationDataSourceImpl @Inject constructor(private val networkApi: DataGoKrNetworkApi) : GranuleIdentificationDataSource {

    override fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfoResponse>> = channelFlow {
        networkApi.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).onResponse().fold(
            onSuccess = { response ->
                response.toResult().onSuccess {
                    if (it.body.items.isNotEmpty()) send(Result.success(it))
                    else send(Result.failure(Throwable("No Data")))
                }.onFailure {
                    send(Result.failure(it))
                }
            },
            onFailure = {
                send(Result.failure(it))
            },
        )
    }
}
