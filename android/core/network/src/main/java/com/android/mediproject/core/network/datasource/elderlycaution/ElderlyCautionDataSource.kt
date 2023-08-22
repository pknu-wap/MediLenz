package com.android.mediproject.core.network.datasource.elderlycaution

import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionResponse
import retrofit2.http.Query

interface ElderlyCautionDataSource {

    /**
     * 의약품 노인주의 정보 조회
     *
     * @param itemName 의약품명
     * @param itemSeq 의약품 고유번호
     */
    suspend fun getElderlyCaution(
        @Query("itemName") itemName: String?,
        @Query("itemSeq") itemSeq: String?,
    ): Result<ElderlyCautionResponse>
}