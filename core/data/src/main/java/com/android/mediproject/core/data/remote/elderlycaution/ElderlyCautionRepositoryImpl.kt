package com.android.mediproject.core.data.remote.elderlycaution

import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionResponse
import com.android.mediproject.core.network.datasource.elderlycaution.ElderlyCautionDataSource
import javax.inject.Inject

class ElderlyCautionRepositoryImpl : ElderlyCautionRepository {

    @Inject private lateinit var dataSource: ElderlyCautionDataSource

    override suspend fun getElderlyCaution(itemName: String?, itemSeq: String?): Result<ElderlyCautionResponse.Body.Item> =
        dataSource.getElderlyCaution(itemName = itemName, itemSeq = itemSeq).map {
            it.body.items.first()
        }
}