package com.android.mediproject.core.network.datasource.granule

import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface GranuleIdentificationDataSource {

    fun getGranuleIdentificationInfo(
        @Query("item_name") itemName: String?,
        @Query("entp_name") entpName: String?,
        @Query("item_seq") itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfoResponse>>
}