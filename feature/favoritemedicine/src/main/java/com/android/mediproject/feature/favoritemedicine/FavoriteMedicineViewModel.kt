package com.android.mediproject.feature.favoritemedicine

import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicine
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
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

    fun loadTokens() = viewModelScope.launch { getTokenUseCase().collect { _token.value = it } }

    private val _eventFlow = MutableEventFlow<FavoriteMedicineEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun event(event: FavoriteMedicineEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToFavoriteMeidcineMore() = event(FavoriteMedicineEvent.NavigateToFavoriteMedicineMore)

    sealed class FavoriteMedicineEvent {
        object NavigateToFavoriteMedicineMore : FavoriteMedicineEvent()
    }

    private val _favoriteMedicineList = MutableStateFlow<List<FavoriteMedicine>>(listOf())
    val favoriteMedicineList get() = _favoriteMedicineList

    private val _token = MutableStateFlow<TokenState<CurrentTokens>>(TokenState.Empty)
    val token get() = _token.asStateFlow()

    fun loadFavoriteMedicines() =
        viewModelScope.launch {
            getFavoriteMedicineUseCase.getFavoriteMedicineList()
                .collect { it ->
                    it.fold(
                        onSuccess = { _favoriteMedicineList.value = it },
                        onFailure = { },
                    )
                }
        }
}
