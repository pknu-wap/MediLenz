package com.android.mediproject.core.data.remote.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import kotlinx.coroutines.flow.Flow

interface GranuleIdentificationRepository {
    fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfoResponse.Body.Item>>
}