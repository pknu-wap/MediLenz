package com.android.mediproject.core.data.dur.durproduct

import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.core.model.dur.DurType
import kotlinx.coroutines.flow.Flow

interface DurProductRepository {

    fun hasDur(itemName: String?, itemSeq: String?): Flow<Result<List<DurType>>>

    fun getDur(itemSeq: String, durTypes: List<DurType>): Flow<Map<DurType, Result<List<DurItem>>>>
}
