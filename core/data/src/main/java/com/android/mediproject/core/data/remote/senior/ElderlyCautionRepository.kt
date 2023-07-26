package com.android.mediproject.core.data.remote.senior

interface ElderlyCautionRepository {
    suspend fun getElderlyCaution(
        itemName: String?,
        itemSeq: String?,
    ): Result<DurProductElderlyCautionResponse.Body.Item>
}
