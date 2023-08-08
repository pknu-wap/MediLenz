package com.android.mediproject.feature.news.safetynotification

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetSafetyNotificationUseCase
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.model.news.safetynotification.SafetyNotification
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SafetyNotificationViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val _dispatchers: CoroutineDispatcher,
    getSafetyNotificationUseCase: GetSafetyNotificationUseCase,
) : BaseViewModel() {
    val dispatchers get() = _dispatchers
    val safetyNotificationList: Flow<PagingData<SafetyNotification>> = getSafetyNotificationUseCase().cachedIn(viewModelScope).map { pagingData ->
        pagingData.map { source ->
            val wrapper = UiModelMapperFactory.create<SafetyNotification>(source)
            wrapper.convert().apply {
                onClick = ::onClick
            }
        }
    }.flowOn(dispatchers)

    private fun onClick(safetyNotification: SafetyNotification) {
        viewModelScope.launch {

        }
    }
}
