package com.android.mediproject.core.data.remote.recallsuspension

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecallSuspensionRepositoryImpl @Inject constructor(
    private val recallSuspensionDataSource: RecallSuspensionDataSource,
) :
    RecallSuspensionRepository {

    override suspend fun getRecallDisposalList(): Flow<PagingData<RecallSuspensionListResponse.Body.Item.Item>> = Pager(
        config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE),
        pagingSourceFactory = {
            RecallSuspensionListDataSourceImpl(
                recallSuspensionDataSource
            )
        }
    ).flow

    override suspend fun getDetailRecallSuspension(
        company: String?,
        product: String?
    ): Result<DetailRecallSuspensionResponse.Body.Item.Item> {
        return recallSuspensionDataSource.getDetailRecallSuspensionInfo(
            pageNo = 1,
            company = company,
            product = product
        )
    }
}