package com.android.mediproject.core.data.remote.recallsuspension

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import kotlinx.coroutines.flow.Flow

interface RecallSuspensionRepository {
    fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspensionListResponse.Body.Item.Item>>

    suspend fun getRecentRecallDisposalList(
        pageNo: Int = 1, numOfRows: Int = 15
    ): Result<List<RecallSuspensionListResponse.Body.Item.Item>>

    fun getDetailRecallSuspension(
        company: String?, product: String?
    ): Flow<Result<DetailRecallSuspensionResponse.Body.Item.Item>>
}