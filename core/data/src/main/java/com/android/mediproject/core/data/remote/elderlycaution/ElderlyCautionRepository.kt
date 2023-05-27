package com.android.mediproject.core.data.remote.elderlycaution

import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionResponse

interface ElderlyCautionRepository {
    suspend fun getElderlyCaution(
        itemName: String?,
        itemSeq: String?,
    ): Result<ElderlyCautionResponse.Body.Item>
}