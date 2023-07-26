package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspension
import com.android.mediproject.core.model.remote.recall.RecallSuspension
import com.android.mediproject.core.model.remote.recall.toRecallSuspension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecallSuspensionInfoUseCase @Inject constructor(
    private val recallSuspensionRepository: RecallSuspensionRepository,
) {

    fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspension>> = recallSuspensionRepository.getRecallDisposalList().let { pager ->
        pager.map { pagingData ->
            pagingData.map {
                it.toRecallSuspension()
            }
        }
    }

    suspend fun getRecentRecallDisposalList(
        pageNo: Int = 1, numOfRows: Int = 15,
    ): Result<List<RecallSuspension>> = recallSuspensionRepository.getRecentRecallDisposalList(pageNo, numOfRows).map {
        it.map { item ->
            item.toRecallSuspension()
        }
    }

    fun getDetailRecallSuspension(
        company: String?, product: String?,
    ): Flow<Result<DetailRecallSuspension>> = recallSuspensionRepository.getDetailRecallSuspension(company, product).map {
        it.fold(
            onSuccess = { item ->
                Result.success(item.toRecallSuspension())
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            },
        )
    }
}
