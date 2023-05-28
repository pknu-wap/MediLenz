package com.android.mediproject.feature.news.adminaction

import androidx.lifecycle.viewModelScope
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetAdminActionInfoUseCase
import com.android.mediproject.core.model.remote.adminaction.AdminActionListItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AdminActionViewModel @Inject constructor(
    private val getAdminActionInfoUseCase: GetAdminActionInfoUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private lateinit var _adminActionList: Flow<PagingData<AdminActionListItemDto>>
    val adminActionList by lazy { _adminActionList }

    private val clickedItemPosition = MutableStateFlow(-1)

    val clickedItem by lazy {
        val getItemFromPaging = GetItemFromPaging(ioDispatcher)
    }

    /**
     * 행정 처분 목록 로드
     */
    init {
        viewModelScope.launch {
            _adminActionList = getAdminActionInfoUseCase.getAdminActionList().flowOn(ioDispatcher).cachedIn(viewModelScope)
        }
    }

    fun onClickedItem(position: Int) {
        clickedItemPosition.value = position
    }

}

class GetItemFromPaging(dispatcher: CoroutineContext) : PagingDataDiffer<AdminActionListItemDto>(DiffCallback, dispatcher) {
    override suspend fun presentNewList(
        previousList: NullPaddedList<AdminActionListItemDto>,
        newList: NullPaddedList<AdminActionListItemDto>,
        lastAccessedIndex: Int,
        onListPresentable: () -> Unit
    ): Int? {
        TODO()
    }

}


object DiffCallback : DifferCallback {
    override fun onChanged(position: Int, count: Int) {
        TODO("Not yet implemented")
    }

    override fun onInserted(position: Int, count: Int) {
        TODO("Not yet implemented")
    }

    override fun onRemoved(position: Int, count: Int) {
        TODO("Not yet implemented")
    }
}