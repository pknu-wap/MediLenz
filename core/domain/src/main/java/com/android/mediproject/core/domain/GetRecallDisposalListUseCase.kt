package com.android.mediproject.core.domain

import androidx.paging.PagingData
import com.android.mediproject.core.model.remote.recalldisposal.RecallDisposalResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecallDisposalListUseCase @Inject constructor() {


    fun invoke(): Flow<PagingData<RecallDisposalResponse>> {

    }
}