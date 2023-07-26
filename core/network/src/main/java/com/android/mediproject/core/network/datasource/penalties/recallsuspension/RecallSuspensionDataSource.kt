package com.android.mediproject.core.network.datasource.penalties.recallsuspension

import com.android.mediproject.core.model.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.recall.RecallSuspensionListResponse
import kotlinx.coroutines.flow.Flow

interface RecallSuspensionDataSource {

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    suspend fun getRecallSuspensionList(
        pageNo: Int, numOfRows: Int = 15
    ): Result<RecallSuspensionListResponse>

    /**
     * 의약품 회수·판매중지 정보 상세 조회
     *
     * @param company 제조사
     * @param product 제품명
     */
    fun getDetailRecallSuspensionInfo(
        company: String?,
        product: String?,
    ): Flow<Result<DetailRecallSuspensionResponse>>
}
