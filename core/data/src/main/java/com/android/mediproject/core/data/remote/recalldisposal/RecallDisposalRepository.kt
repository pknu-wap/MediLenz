package com.android.mediproject.core.data.remote.recalldisposal

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.recalldisposal.RecallDisposalResponse
import kotlinx.coroutines.flow.Flow

interface RecallDisposalRepository {
    fun getRecallDisposalList(
    ): Flow<PagingData<RecallDisposalResponse.Body.Item.Item>>
}