package com.android.mediproject.feature.medicine.precautions.item

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetDurUseCase
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MedicineSafeUsageViewModel @Inject constructor(
    private val getDurUseCase: GetDurUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _elderlyCaution = MutableStateFlow(null)
    val elderlyCaution get() = _elderlyCaution.asStateFlow()


    private val _dur = MutableStateFlow<UiState<Int>>(UiState.Loading)

    val dur get() = _dur.asStateFlow()


    fun loadDur(
        itemName: String?, itemSeq: String?,
    ) {

    }

}
