package com.android.mediproject.feature.news.safetynotification

import androidx.paging.PagingData
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.adminaction.AdminAction
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SafetyNotificationViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val _dispatchers: CoroutineDispatcher,
) : BaseViewModel() {
    val dispatchers get() = _dispatchers
    private lateinit var _adminActionList: Flow<PagingData<AdminAction>>
    val adminActionList by lazy { _adminActionList }
}
