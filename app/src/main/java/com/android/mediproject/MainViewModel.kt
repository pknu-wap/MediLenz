package com.android.mediproject

import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    val lastSelectedBottomNavFragmentIdKey = "lastSelectedBottomNavFragmentId"

    private val _eventFlow = MutableEventFlow<MainEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _selectedBottomNavFragmentId = MutableEventFlow<Int>(replay = 1)
    val selectedBottomNavFragmentId = _selectedBottomNavFragmentId.asEventFlow()

    init {
        viewModelScope.launch {
/*            getAccountStateUseCase.loadAccountState()*/
            if (savedStateHandle.keys().isNotEmpty()) {
                savedStateHandle.get<Int>(lastSelectedBottomNavFragmentIdKey)?.let {
                    _selectedBottomNavFragmentId.emit(it)
                }
            }
        }
    }

    fun getHideBottomNavDestinationIds(resources: Resources): Set<Int> = resources.getArray(R.array.hideBottomNavDestinationIds)

    private fun Resources.getArray(@ArrayRes id: Int): Set<Int> {
        return obtainTypedArray(id).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()
            for (i in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(i, 0))
            }
            typedArray.recycle()
            destinationIds
        }
    }


    fun event(event: MainEvent) = viewModelScope.launch { _eventFlow.emit(event) }
    fun aicamera() = event(MainEvent.AICamera())


    sealed class MainEvent {
        data class AICamera(val unit: Unit? = null) : MainEvent()
    }
}
