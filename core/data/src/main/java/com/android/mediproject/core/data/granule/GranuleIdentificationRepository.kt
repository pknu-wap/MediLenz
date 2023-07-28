package com.android.mediproject.core.data.granule

import com.android.mediproject.core.model.granule.GranuleIdentificationInfoResponse
import kotlinx.coroutines.flow.Flow

interface GranuleIdentificationRepository {
    fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfoResponse.Item>>
}
