package com.android.mediproject.core.data.recallsuspension

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.model.news.recall.RecallSaleSuspensionListResponse
import com.android.mediproject.core.network.datasource.news.recallsuspension.RecallSaleSuspensionDataSource
import com.android.mediproject.core.network.datasource.news.recallsuspension.RecallSaleSuspensionListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecallSaleSuspensionRepositoryImpl @Inject constructor(
    private val recallSaleSuspensionDataSource: RecallSaleSuspensionDataSource,
    private val recallSuspensionListDataSource: RecallSaleSuspensionListDataSourceImpl,
) : RecallSaleSuspensionRepository {

    override fun getRecallSaleSuspensionList(): Flow<PagingData<RecallSaleSuspensionListResponse.Item.Item>> = Pager(
        config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE),
        pagingSourceFactory = {
            recallSuspensionListDataSource
        },
    ).flow

    override suspend fun getRecentRecallSaleSuspensionList(
        pageNo: Int, numOfRows: Int,
    ): Result<List<RecallSaleSuspensionListResponse.Item.Item>> = recallSaleSuspensionDataSource.getRecallSaleSuspensionList(
        pageNo, numOfRows,
    ).map { response ->
        response.body.items.map {
            it.item
        }
    }

    override suspend fun getDetailRecallSaleSuspension(
        company: String?, product: String?,
    ): Result<DetailRecallSaleSuspensionResponse.Item.Item> = recallSaleSuspensionDataSource.getDetailRecallSaleSuspension(
        company = company, product = product,
    ).fold(
        onSuccess = {
            Result.success(it.body.items.first().item)
        },
        onFailure = {
            Result.failure(it)
        },
    )

}
