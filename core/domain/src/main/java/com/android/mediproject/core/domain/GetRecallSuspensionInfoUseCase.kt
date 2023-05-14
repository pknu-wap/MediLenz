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

    suspend fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspensionListItemDto>> = recallSuspensionRepository.getRecallDisposalList().let { pager ->
        pager.map { pagingData ->
            pagingData.map {
                it.toDto()
            }
        }
    }

    suspend fun getDetailRecallSuspension(
        company: String?, product: String?
    ): Result<DetailRecallSuspensionItemDto> = recallSuspensionRepository.getDetailRecallSuspension(company, product).map {
        it.toDto()
    }
}