package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.toResult
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class DurDataSourceImpl @Inject constructor(
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
) : DurDataSource {

    override fun getDur(itemName: String?, itemSeq: String?) = channelFlow {
        dataGoKrNetworkApi.getDur(itemName = itemName, itemSeq = itemSeq).onResponse().fold(
            onSuccess = { response ->
                response.toResult()
            },
            onFailure = {
                Result.failure(it)
            },
        ).also {
            send(it)
        }
    }
}
