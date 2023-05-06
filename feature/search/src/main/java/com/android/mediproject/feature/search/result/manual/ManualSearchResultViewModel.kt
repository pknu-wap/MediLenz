package com.android.mediproject.feature.search.result.manual

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualSearchResultViewModel @Inject constructor(
    private val getMedicineApprovalListUseCase: GetMedicineApprovalListUseCase
) : BaseViewModel() {

    private val _searchQuery = MutableEventFlow<String>(replay = 1)

    val searchResultFlow: Flow<PagingData<ApprovedMedicineItemDto>> = _searchQuery.flatMapLatest { query ->
        getMedicineApprovalListUseCase.invoke(
            itemName = query,
            entpName = null,
        ).let { pager ->
            pager.map { pagingData ->
                pagingData.map { item ->
                    item.onClick = this@ManualSearchResultViewModel::openMedicineInfo
                    item
                }
            }
        }.cachedIn(viewModelScope)
    }

    fun searchMedicines(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    /**
     * 약 정보 화면으로 이동
     */
    fun openMedicineInfo(approvedMedicineItemDto: ApprovedMedicineItemDto) {

    }
}