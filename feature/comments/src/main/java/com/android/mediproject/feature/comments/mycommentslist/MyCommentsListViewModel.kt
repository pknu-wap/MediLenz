package com.android.mediproject.feature.comments.mycommentslist

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCommentsListViewModel @Inject constructor() : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MyCommentsListEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: MyCommentsListEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    sealed class MyCommentsListEvent
}
