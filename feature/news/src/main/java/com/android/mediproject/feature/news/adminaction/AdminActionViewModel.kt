package com.android.mediproject.feature.news.adminaction

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.domain.GetAdminActionInfoUseCase
import com.android.mediproject.core.model.remote.adminaction.AdminActionListItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminActionViewModel @Inject constructor(getAdminActionInfoUseCase: GetAdminActionInfoUseCase) : BaseViewModel() {

    private lateinit var _adminActionList: Flow<PagingData<AdminActionListItemDto>>
    val adminActionList by lazy { _adminActionList }

    /**
     * 행정 처분 목록 로드
     */
    init {
        viewModelScope.launch {
            _adminActionList = getAdminActionInfoUseCase.getAdminActionList(
            ).let { pager ->
                pager.map { pagingData ->
                    pagingData.map { item ->
                        item.onClick = this@AdminActionViewModel::openDetail
                        item
                    }
                }
            }.cachedIn(viewModelScope)
        }
    }

    private fun openDetail(item: AdminActionListItemDto) {

    }

}