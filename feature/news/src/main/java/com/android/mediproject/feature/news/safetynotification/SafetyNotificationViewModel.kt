package com.android.mediproject.feature.news.safetynotification

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetSafetyNotificationUseCase
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.model.news.safetynotification.SafetyNotification
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SafetyNotificationViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    getSafetyNotificationUseCase: GetSafetyNotificationUseCase,
) : BaseViewModel() {

    val listScrollState = mutableStateOf(0 to 0)
    val lazyListState: LazyListState = LazyListState(listScrollState.value.first, listScrollState.value.second)

    private val _clickedItem: MutableStateFlow<UiState<SafetyNotification>> = MutableStateFlow(UiState.Initial)
    val clickedItem = _clickedItem.asStateFlow()

    val safetyNotificationList: Flow<PagingData<SafetyNotification>> = getSafetyNotificationUseCase().map { pagingData ->
        pagingData.map { source ->
            val wrapper = UiModelMapperFactory.create<SafetyNotification>(source)
            wrapper.convert()
        }
    }.cachedIn(viewModelScope).flowOn(ioDispatcher)

    fun onClick(safetyNotification: SafetyNotification) {
        viewModelScope.launch {
            _clickedItem.value = UiState.Success(safetyNotification)
        }
    }
}
