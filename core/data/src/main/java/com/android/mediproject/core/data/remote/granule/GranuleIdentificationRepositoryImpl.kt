package com.android.mediproject.core.data.remote.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import javax.inject.Inject

class GranuleIdentificationRepositoryImpl @Inject constructor(
    private val dataSource: GranuleIdentificationDataSource
) : GranuleIdentificationRepository {


    override suspend fun getGranuleIdentificationInfo(
        itemName: String?, entpName: String?, itemSeq: String?
    ): Result<GranuleIdentificationInfoResponse.Body.Item> =
        dataSource.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).map {
            it.body.items.first()
        }
}