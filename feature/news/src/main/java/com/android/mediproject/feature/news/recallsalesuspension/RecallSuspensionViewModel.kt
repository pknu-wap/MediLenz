package com.android.mediproject.feature.news.recallsalesuspension

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetRecallSaleSuspensionUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class RecallSuspensionViewModel @Inject constructor(
    getRecallSaleSuspensionUseCase: GetRecallSaleSuspensionUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    val recallDisposalList = getRecallSaleSuspensionUseCase.getRecallSaleSuspensionList().cachedIn(viewModelScope).flowOn(ioDispatcher)

    val listScrollState = mutableStateOf(0)
}
