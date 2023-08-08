package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.recallsuspension.RecallSaleSuspensionRepository
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.model.news.recall.DetailRecallSuspension
import com.android.mediproject.core.model.news.recall.RecallSaleSuspension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecallSaleSuspensionUseCase @Inject constructor(
    private val recallSaleSuspensionRepository: RecallSaleSuspensionRepository,
) {

    fun getRecallSaleSuspensionList(
    ): Flow<PagingData<RecallSaleSuspension>> = recallSaleSuspensionRepository.getRecallSaleSuspensionList().let { pager ->
        pager.map { pagingData ->
            pagingData.map {
                UiModelMapperFactory.create<RecallSaleSuspension>(it).convert()
            }
        }
    }

    suspend fun getRecentRecallSaleSuspensionList(
        pageNo: Int = 1, numOfRows: Int = 15,
    ): Result<List<RecallSaleSuspension>> = recallSaleSuspensionRepository.getRecentRecallSaleSuspensionList(pageNo, numOfRows).map {
        it.map { item ->
            UiModelMapperFactory.create<RecallSaleSuspension>(item).convert()
        }
    }

    fun getDetailRecallSaleSuspension(
        company: String?, product: String?,
    ): Flow<Result<DetailRecallSuspension>> = recallSaleSuspensionRepository.getDetailRecallSaleSuspension(company, product).map {
        it.fold(
            onSuccess = { item ->
                Result.success(
                    UiModelMapperFactory.create<DetailRecallSuspension>(item).convert(),
                )
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            },
        )
    }
}
