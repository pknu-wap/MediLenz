package com.android.mediproject.feature.favoritemedicine

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMedicineViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getFavoriteMedicineUseCase: GetFavoriteMedicineUseCase,
) : BaseViewModel() {

    init {
        loadTokens()
    }

    fun loadTokens() = viewModelScope.launch { getTokenUseCase().collect { _token.value = it } }

    private val _eventFlow = MutableEventFlow<FavoriteMedicineEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: FavoriteMedicineEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToFavoriteMeidcineMore() = event(FavoriteMedicineEvent.NavigateToFavoriteMedicineMore)

    sealed class FavoriteMedicineEvent {
        object NavigateToFavoriteMedicineMore : FavoriteMedicineEvent()
    }

    private val _favoriteMedicineList = MutableStateFlow<List<FavoriteMedicineDto>>(listOf())
    val favoriteMedicineList get() = _favoriteMedicineList

    private val _token = MutableStateFlow<TokenState<CurrentTokenDto>>(TokenState.Empty)
    val token get() = _token.asStateFlow()

    fun loadFavoriteMedicines() =
        viewModelScope.launch {
            getFavoriteMedicineUseCase.getFavoriteMedicineList()
                .collect {
                    it.fold(
                        onSuccess = { _favoriteMedicineList.value = it },
                        onFailure = { },
                    )
                }
        }
}
