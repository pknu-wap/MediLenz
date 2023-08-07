package com.android.mediproject.feature.news.recallsalesuspension

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetRecallSuspensionInfoUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class RecallSuspensionViewModel @Inject constructor(
    getRecallSuspensionInfoUseCase: GetRecallSuspensionInfoUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    val recallDisposalList = getRecallSuspensionInfoUseCase.getRecallDisposalList().cachedIn(viewModelScope).flowOn(ioDispatcher)

}
