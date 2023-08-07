package com.android.mediproject.feature.news.adminaction

import androidx.lifecycle.viewModelScope
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingDataDiffer
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetAdminActionInfoUseCase
import com.android.mediproject.core.model.adminaction.AdminAction
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class AdminActionViewModel @Inject constructor(
    getAdminActionInfoUseCase: GetAdminActionInfoUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    val adminActionList = getAdminActionInfoUseCase.getAdminActionList().cachedIn(viewModelScope).flowOn(ioDispatcher)
    private val clickedItemPosition = MutableStateFlow(-1)

    fun onClickedItem(position: Int) {
        clickedItemPosition.value = position
    }

    private val _clickedItem = MutableStateFlow<AdminAction?>(null)
    val clickedItem get() = _clickedItem.asStateFlow()

    fun getClickedItem() {
        viewModelScope.launch(defaultDispatcher) {
            adminActionList.last().run {
                WeakReference(
                    object : PagingDataDiffer<AdminAction>(
                        differCallback = object : DifferCallback {
                            override fun onChanged(position: Int, count: Int) {

                            }

                            override fun onInserted(position: Int, count: Int) {
                            }

                            override fun onRemoved(position: Int, count: Int) {
                            }

                        },
                        mainContext = defaultDispatcher,
                        cachedPagingData = this,
                    ) {
                        override suspend fun presentNewList(
                            previousList: NullPaddedList<AdminAction>,
                            newList: NullPaddedList<AdminAction>,
                            lastAccessedIndex: Int,
                            onListPresentable: () -> Unit,
                        ) = null
                    },
                ).get()?.run {
                    _clickedItem.value = this[clickedItemPosition.value]
                }
            }
        }
    }
}
