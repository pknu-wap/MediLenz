package com.android.mediproject.core.data.remote.dur

import kotlinx.coroutines.flow.Flow

interface DurRepository {
    suspend fun getDur(
        itemName: String?,
        itemSeq: String?,
    ): Flow<Result<DurResponse.Body.Item>>
}
