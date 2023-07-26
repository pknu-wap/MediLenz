package com.android.mediproject.feature.news.recallsuspension

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetRecallSuspensionInfoUseCase
import com.android.mediproject.core.model.recall.RecallSuspension
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecallSuspensionViewModel @Inject constructor(
    private val getRecallSuspensionInfoUseCase: GetRecallSuspensionInfoUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private lateinit var _recallDisposalList: Flow<PagingData<RecallSuspension>>
    val recallDisposalList by lazy { _recallDisposalList }

    /**
     * 회수 폐기 공고 목록을 로드
     */
    init {
        viewModelScope.launch {
            _recallDisposalList = getRecallSuspensionInfoUseCase.getRecallDisposalList().flowOn(ioDispatcher).cachedIn(viewModelScope)
        }
    }


}
