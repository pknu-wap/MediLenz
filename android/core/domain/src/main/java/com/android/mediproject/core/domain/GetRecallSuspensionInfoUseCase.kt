package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionItemDto
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.model.remote.recall.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecallSuspensionInfoUseCase @Inject constructor(
    private val recallSuspensionRepository: RecallSuspensionRepository
) {

    fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspensionListItemDto>> =
        recallSuspensionRepository.getRecallDisposalList().let { pager ->
            pager.map { pagingData ->
                pagingData.map {
                    it.toDto()
                }
            }
        }

    suspend fun getRecentRecallDisposalList(
        pageNo: Int = 1, numOfRows: Int = 15
    ): Result<List<RecallSuspensionListItemDto>> =
        recallSuspensionRepository.getRecentRecallDisposalList(pageNo, numOfRows).map {
            it.map { item ->
                item.toDto()
            }
        }

    fun getDetailRecallSuspension(
        company: String?, product: String?
    ): Flow<Result<DetailRecallSuspensionItemDto>> =
        recallSuspensionRepository.getDetailRecallSuspension(company, product).map {
            it.fold(onSuccess = { item ->
                Result.success(item.toDto())
            }, onFailure = { throwable ->
                Result.failure(throwable)
            })
        }
}