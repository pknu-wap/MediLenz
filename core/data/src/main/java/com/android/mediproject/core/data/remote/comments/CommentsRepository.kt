package com.android.mediproject.core.data.remote.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    suspend fun getCommentsForAMedicine(
        itemSeq: String,
    ): Flow<PagingData<MedicineCommentsResponse>>
}