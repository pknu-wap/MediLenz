package com.android.mediproject.feature.search.recentsearchlist

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.SearchHistoryUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.search.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchListViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher) : BaseViewModel() {

    private val _noHistoryText = MutableStateFlow<Spanned>(SpannableStringBuilder())
    val noHistoryText get() = _noHistoryText.asStateFlow()

    fun createNoHistoryText(context: Context) {
        viewModelScope.launch(defaultDispatcher) {
            val span = SpannableStringBuilder(context.getString(R.string.noHistoryOfSearch)).apply {
                setSpan(ForegroundColorSpan(ContextCompat.getColor(context, com.android.mediproject.core.ui.R.color.main)),
                    7,
                    9,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(UnderlineSpan(), 7, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            _noHistoryText.value = span
        }
    }

    val searchHistoryList by lazy {
        suspend {
            searchHistoryUseCase.getSearchHistoryList(6).flatMapLatest {
                flowOf(UiState.Success(it))
            }.catch {
                flowOf(UiState.Error(it.message ?: "Error"))
            }.stateIn(viewModelScope)
        }
    }


}