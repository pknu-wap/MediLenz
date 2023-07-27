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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@HiltViewModel
class DurInfoViewModel @Inject constructor(
    private val getDurUseCase: GetDurUseCase,
    private val durTextMapper: DurTextMapper,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _durList = MutableStateFlow<State>(State.Initial)
    val durList = _durList.asStateFlow()

    fun getDur(itemSeq: String) {
        viewModelScope.launch(defaultDispatcher) {
            val durTitleAndDescriptions = async {
                durTextMapper.durTitleAndDescriptions.filterNotNull().first()
            }

            val durTypes = getDurUseCase.hasDur(itemName = null, itemSeq = itemSeq)
            durTypes.onSuccess {
                _durList.value = State.LoadingDurTypes

                val durTitleAndDescriptionMap = durTitleAndDescriptions.await()
                val durTypePos = mutableMapOf<DurType, Int>()

                val durItemList = it.mapIndexed { i, durType ->
                    durTypePos[durType] = i
                    DurListItem(
                        durType, title = durTitleAndDescriptionMap.getValue(durType).title,
                        description = durTitleAndDescriptionMap.getValue(durType).description,
                    )
                }

                val durListResult = getDurUseCase.getDur(itemSeq, it)

                durListResult.forEach { (durType, result) ->
                    durItemList[durTypePos.getValue(durType)].durItems = result
                }

                _durList.value = State.Success(durItemList)
            }.onFailure {
                _durList.value = State.Error(it.message ?: "의약품 안전 사용 정보를 불러오는 중 오류가 발생했습니다.")
            }
        }
    }
}


sealed interface State {
    object Initial : State
    object LoadingDurTypes : State
    object LoadingDurList : State
    data class Success(val durItemList: List<DurListItem>) : State
    data class Error(val message: String) : State
}

@OptIn(ExperimentalContracts::class)
fun State.isInitializing(block: () -> Unit): State {
    contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    if (this is State.Initial) block()
    return this
}

@OptIn(ExperimentalContracts::class)
fun State.isLoadingDurTypes(block: () -> Unit): State {
    contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    if (this is State.LoadingDurTypes) block()
    return this
}

@OptIn(ExperimentalContracts::class)
fun State.isLoadingDurList(block: () -> Unit): State {
    contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    if (this is State.LoadingDurList) block()
    return this
}

@OptIn(ExperimentalContracts::class)
fun State.isSuccess(block: (List<DurListItem>) -> Unit): State {
    contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    if (this is State.Success) block(durItemList)
    return this
}

@OptIn(ExperimentalContracts::class)
fun State.isError(block: (String) -> Unit): State {
    contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    if (this is State.Error) block(message)
    return this
}
