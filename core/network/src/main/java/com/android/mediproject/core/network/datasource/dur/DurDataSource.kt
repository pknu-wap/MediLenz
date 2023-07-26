package com.android.mediproject.core.network.datasource.dur

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface DurDataSource {
    fun getDur(
        @Query("item_name") itemName: String?,
        @Query("item_seq") itemSeq: String?,
    ): Flow<Result<DurResponse>>
}
