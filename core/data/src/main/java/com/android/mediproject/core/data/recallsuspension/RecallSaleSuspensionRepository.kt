package com.android.mediproject.core.data.recallsuspension

import androidx.paging.PagingData
import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.model.news.recall.RecallSaleSuspensionListResponse
import kotlinx.coroutines.flow.Flow

interface RecallSaleSuspensionRepository {
    fun getRecallSaleSuspensionList(
    ): Flow<PagingData<RecallSaleSuspensionListResponse.Item.Item>>

    suspend fun getRecentRecallSaleSuspensionList(
        pageNo: Int = 1, numOfRows: Int = 15,
    ): Result<List<RecallSaleSuspensionListResponse.Item.Item>>

    suspend fun getDetailRecallSaleSuspension(
        company: String?, product: String?,
    ): Result<DetailRecallSaleSuspensionResponse.Item.Item>
}
