package com.android.mediproject.core.network.datasource.elderlycaution

import com.android.mediproject.core.model.toResult
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import javax.inject.Inject

class ElderlyCautionDataSourceImpl @Inject constructor(private val dataGoKrNetworkApi: DataGoKrNetworkApi) : ElderlyCautionDataSource {


    override suspend fun getElderlyCaution(itemName: String?, itemSeq: String?): Result<DurProductElderlyCautionResponse> =
        dataGoKrNetworkApi.getElderlyCaution(itemName = itemName, _itemSeq = itemSeq).onResponse().fold(
            onSuccess = { response ->
                response.toResult()
            },
            onFailure = {
                Result.failure(it)
            },
        )

}
