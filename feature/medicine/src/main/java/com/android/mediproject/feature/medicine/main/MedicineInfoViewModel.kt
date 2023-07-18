package com.android.mediproject.feature.medicine.main

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.CommentsUseCase
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineInfoViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val commentsUseCase: CommentsUseCase,
    private val getFavoriteMedicineUseCase: GetFavoriteMedicineUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            // 댓글 업데이트 시 스크롤을 맨 아래로 내립니다.
            commentsUseCase.scrollChannel.receiveAsFlow().shareIn(viewModelScope, SharingStarted.Eagerly, replay = 0).collect {
                _eventState.emit(EventState.ScrollToBottom)
            }
        }
    }

    private val _eventState = MutableEventFlow<EventState>(replay = 1)
    val eventState get() = _eventState.asEventFlow()

    private val _medicinePrimaryInfo = MutableSharedFlow<MedicineInfoArgs>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val medicinePrimaryInfo get() = _medicinePrimaryInfo.asSharedFlow()

    private val checkFavoriteMedicine = MutableStateFlow(EventState.Favorite(isFavorite = false, lockChecked = true))

    val medicineDetails: StateFlow<UiState<MedicineDetatilInfoDto>> = medicinePrimaryInfo.flatMapLatest { primaryInfo ->
        getMedicineDetailsUseCase(primaryInfo).mapLatest { result ->
            result.fold(
                onSuccess = {
                    UiState.Success(it)
                },
                onFailure = { UiState.Error(it.message ?: "failed") },
            )
        }.flowOn(defaultDispatcher)
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = UiState.Loading)

    fun setMedicinePrimaryInfo(medicineArgs: MedicineInfoArgs) {
        viewModelScope.launch {
            _medicinePrimaryInfo.emit(medicineArgs)
            getMedicineDetailsUseCase.updateImageCache(medicineArgs.itemSeq.toString(), medicineArgs.imgUrl)
        }
    }

    private fun loadFavoriteMedicine(medicineIdInAws: Long) {
        viewModelScope.launch {
            getFavoriteMedicineUseCase.checkFavoriteMedicine(medicineIdInAws).collect { responseResult ->
                responseResult.onSuccess {
                    // 관심약 여부를 보여줍니다.
                    _eventState.emit(EventState.Favorite(it.isFavorite, false))
                }.onFailure {
                    // 로그인이 되지 않았거나 그 외의 문제이므로 관심약 여부를 보여주지 않습니다.
                    // 체크박스를 비활성화 시킵니다.
                    _eventState.emit(EventState.Favorite(lockChecked = true))
                }
            }
        }
    }

    fun checkFavoriteMedicine() {
        viewModelScope.launch {
            if (!checkFavoriteMedicine.value.lockChecked) {
                val newState = !checkFavoriteMedicine.value.isFavorite
                getFavoriteMedicineUseCase.favoriteMedicine(medicinePrimaryInfo.replayCache.last().itemSeq, newState)
                    .collect { responseResult ->
                        responseResult.onSuccess {
                            _eventState.emit(EventState.Favorite(newState, false))
                        }.onFailure {
                            _eventState.emit(EventState.Favorite(lockChecked = true))
                        }
                    }
            }
        }
    }

    fun scrollToBottom() {
        viewModelScope.launch {
            _eventState.emit(EventState.ScrollToBottom)
        }
    }
}

sealed class EventState {
    data class Favorite(val isFavorite: Boolean = false, val lockChecked: Boolean) : EventState()
    object ScrollToBottom : EventState()

}
