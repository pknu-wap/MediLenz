package com.android.mediproject

import MutableEventFlow
import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.sign.GetAccountStateUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccountStateUseCase: GetAccountStateUseCase,
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<MainEvent>()
    val eventFlow = _eventFlow.asEventFlow()


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

    init {
        viewModelScope.launch {
            getAccountStateUseCase.loadAccountState()
        }
    }

    sealed class MainEvent {
        data class AICamera(val unit: Unit? = null) : MainEvent()
    }
}
