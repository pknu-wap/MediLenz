package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineMoreInfo
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMedicineMoreViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getFavoriteMedicineUseCase: GetFavoriteMedicineUseCase,
) : BaseViewModel() {

    private val _favoriteMedicineList = MutableStateFlow<List<FavoriteMedicineMoreInfo>>(listOf())
    val favoriteMedicineList get() = _favoriteMedicineList

    private val _token = MutableSharedFlow<TokenState<CurrentTokens>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val token get() = _token.asSharedFlow()

    fun loadTokens() = viewModelScope.launch {
        getTokenUseCase().collect {
            _token.emit(it)
        }
    }
    fun loadFavoriteMedicines() =
        viewModelScope.launch {
            getFavoriteMedicineUseCase.getFavoriteMedicineMoreList()
                .collect { result ->
                    result.fold(
                        onSuccess = { _favoriteMedicineList.value = it },
                        onFailure = { },
                    )
                }
        }
}
