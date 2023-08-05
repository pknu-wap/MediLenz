package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.remote.dur.DurResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface DurDataSource {
    suspend fun getDur(
        @Query("item_name") itemName: String?,
        @Query("item_seq") itemSeq: String?,
    ): Flow<Result<DurResponse>>
}