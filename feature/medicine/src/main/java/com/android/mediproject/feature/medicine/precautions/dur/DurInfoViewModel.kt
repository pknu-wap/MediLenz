package com.android.mediproject.feature.medicine.precautions.dur

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetDurUseCase
import com.android.mediproject.core.model.dur.DurListItem
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DurInfoViewModel @Inject constructor(
    private val getDurUseCase: GetDurUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _durList = MutableStateFlow<State>(State.Initial)
    val durList = _durList.asStateFlow()

    fun getDur(itemSeq: String) {
        viewModelScope.launch(defaultDispatcher) {
            val durTypes = getDurUseCase.hasDur(itemName = null, itemSeq = itemSeq)
            durTypes.onSuccess {
                val durTypePos = mutableMapOf<DurType, Int>()
                val durItemList = it.mapIndexed { i, durType ->
                    durTypePos[durType] = i
                    DurListItem(durType)
                }

                _durList.value = State.LoadingDurTypes(durItemList)
                val durListResult = getDurUseCase.getDur(itemSeq, it)

                durListResult.forEach { (durType, result) ->
                    durItemList[durTypePos.getValue(durType)].durItems = result
                }

                _durList.value = State.Success
            }.onFailure {
                _durList.value = State.Error(it.message ?: "의약품 안전 사용 정보를 불러오는 중 오류가 발생했습니다.")
            }
        }
    }
}


sealed interface State {
    object Initial : State
    data class LoadingDurTypes(val durItemList: List<DurListItem>) : State
    object LoadingDurList : State
    object Success : State
    data class Error(val message: String) : State
}
