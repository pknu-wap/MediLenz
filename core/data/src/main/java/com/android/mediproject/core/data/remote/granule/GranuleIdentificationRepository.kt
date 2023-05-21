package com.android.mediproject.core.data.remote.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse

interface GranuleIdentificationRepository {
    suspend fun getGranuleIdentificationInfo(
        itemName: String?,
        entpName: String?,
        itemSeq: String?,
    ): Result<GranuleIdentificationInfoResponse.Body.Item>
}