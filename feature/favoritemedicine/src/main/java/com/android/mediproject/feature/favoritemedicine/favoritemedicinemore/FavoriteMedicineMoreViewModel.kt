package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.domain.GetFavoriteMedicineUseCase
import com.android.mediproject.core.domain.GetTokenUseCase
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineMoreInfo
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _token = MutableStateFlow<TokenState<CurrentTokens>>(TokenState.Empty)
    val token get() = _token.asStateFlow()

    fun loadTokens() = viewModelScope.launch { getTokenUseCase().collect { _token.value = it } }

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
