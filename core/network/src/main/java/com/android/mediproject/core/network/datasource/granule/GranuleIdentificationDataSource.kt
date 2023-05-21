package com.android.mediproject.core.network.datasource.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import retrofit2.http.Query

interface GranuleIdentificationDataSource {

    suspend fun getGranuleIdentificationInfo(
        @Query("item_name") itemName: String?,
        @Query("entp_name") entpName: String?,
        @Query("item_seq") itemSeq: String?,
    ): Result<GranuleIdentificationInfoResponse>
}