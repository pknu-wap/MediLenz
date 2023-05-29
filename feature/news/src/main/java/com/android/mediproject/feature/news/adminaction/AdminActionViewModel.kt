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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class AdminActionViewModel @Inject constructor(
    private val getAdminActionInfoUseCase: GetAdminActionInfoUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private lateinit var _adminActionList: Flow<PagingData<AdminActionListItemDto>>
    val adminActionList by lazy { _adminActionList }

    private val clickedItemPosition = MutableStateFlow(-1)

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

    private val _clickedItem = MutableStateFlow<AdminActionListItemDto?>(null)
    val clickedItem get() = _clickedItem.asStateFlow()

    fun getClickedItem() {
        viewModelScope.launch(defaultDispatcher) {
            adminActionList.collectLatest {
                WeakReference(object : PagingDataDiffer<AdminActionListItemDto>(
                    differCallback = object : DifferCallback {
                        override fun onChanged(position: Int, count: Int) {

                        }

                        override fun onInserted(position: Int, count: Int) {
                        }

                        override fun onRemoved(position: Int, count: Int) {
                        }

                    },
                    mainContext = defaultDispatcher,
                    cachedPagingData = it
                ) {
                    override suspend fun presentNewList(
                        previousList: NullPaddedList<AdminActionListItemDto>,
                        newList: NullPaddedList<AdminActionListItemDto>,
                        lastAccessedIndex: Int,
                        onListPresentable: () -> Unit
                    ) = null
                }).get()?.apply {
                    _clickedItem.value = this[clickedItemPosition.value]
                }
            }
        }
    }
}