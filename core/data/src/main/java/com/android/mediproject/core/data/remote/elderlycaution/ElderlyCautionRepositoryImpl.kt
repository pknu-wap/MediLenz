package com.android.mediproject.core.data.remote.elderlycaution

import com.android.mediproject.core.network.datasource.elderlycaution.ElderlyCautionDataSource
import javax.inject.Inject

class ElderlyCautionRepositoryImpl @Inject constructor(private val dataSource: ElderlyCautionDataSource) : ElderlyCautionRepository {

    override suspend fun getElderlyCaution(itemName: String?, itemSeq: String?): Result<DurProductElderlyCautionResponse.Body.Item> =
        dataSource.getElderlyCaution(itemName = itemName, itemSeq = itemSeq).map {
            it.body.items.first()
        }
}
