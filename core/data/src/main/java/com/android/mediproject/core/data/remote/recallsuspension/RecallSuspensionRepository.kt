package com.android.mediproject.core.data.remote.recallsuspension

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import kotlinx.coroutines.flow.Flow

interface RecallSuspensionRepository {
    suspend fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspensionListResponse.Body.Item.Item>>

    suspend fun getDetailRecallSuspension(
        company: String?, product: String?
    ): Result<DetailRecallSuspensionResponse.Body.Item.Item>
}