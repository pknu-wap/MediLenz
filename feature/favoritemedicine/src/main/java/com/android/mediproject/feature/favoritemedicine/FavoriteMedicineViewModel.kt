package com.android.mediproject.feature.favoritemedicine

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicine
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMedicineViewModel @Inject constructor(
    private val getFavoriteMedicineUseCase: GetFavoriteMedicineUseCase,
) : BaseViewModel() {

    private val _eventFlow = MutableEventFlow<FavoriteMedicineEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: FavoriteMedicineEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToFavoriteMeidcineMore() = event(FavoriteMedicineEvent.NavigateToFavoriteMedicineMore)

    sealed class FavoriteMedicineEvent {
        data object NavigateToFavoriteMedicineMore : FavoriteMedicineEvent()
    }

    private val _favoriteMedicineList = MutableStateFlow<UiState<List<FavoriteMedicine>>>(UiState.Initial)
    val favoriteMedicineList get() = _favoriteMedicineList

    fun setFavoriteMedicineListUiState(uiState: UiState<List<FavoriteMedicine>>) {
        _favoriteMedicineList.value = uiState
    }

    private val _token = MutableSharedFlow<TokenState<CurrentTokens>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val token get() = _token.asSharedFlow()

    fun loadTokens() = viewModelScope.launch {
        /*     getTokenUseCase().collect {
                 _token.emit(it)
             }*/
    }

    fun loadFavoriteMedicines() {
        setFavoriteMedicineListUiState(UiState.Loading)
        viewModelScope.launch {
            getFavoriteMedicineUseCase.getFavoriteMedicineList()
                .collect { it ->
                    it.fold(
                        onSuccess = { setFavoriteMedicineListUiState(UiState.Success(it)) },
                        onFailure = { setFavoriteMedicineListUiState(UiState.Error("즐겨찾기 항목을 불러오는데 실패하였습니다.")) },
                    )
                }
        }
    }
}
