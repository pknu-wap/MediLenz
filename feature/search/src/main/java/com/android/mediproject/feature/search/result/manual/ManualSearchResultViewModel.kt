package com.android.mediproject.feature.search.result.manual

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.constants.MedicationType
import com.android.mediproject.core.model.parameters.ApprovalListSearchParameter
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수동 검색 결과 ViewModel
 *
 * @property getMedicineApprovalListUseCase 약품 목록 조회 UseCase
 * @property _searchParameter 검색 파라미터
 * @property searchResultFlow 검색 결과 Flow
 */
@HiltViewModel
class ManualSearchResultViewModel @Inject constructor(
    private val getMedicineApprovalListUseCase: GetMedicineApprovalListUseCase,
) : BaseViewModel() {

    private val _searchParameter = MutableStateFlow(
        ApprovalListSearchParameter(
            itemName = null, entpName = null, medicationType = MedicationType.ALL
        )
    )
    val searchParameter = _searchParameter.asStateFlow()

    val searchResultFlow: Flow<PagingData<ApprovedMedicineItemDto>> = searchParameter.flatMapLatest { parameter ->
        getMedicineApprovalListUseCase.invoke(
            parameter
        ).cachedIn(viewModelScope)
    }

    /**
     * 의약품명으로 검색
     */
    fun searchMedicinesByItemName(itemName: String) {
        viewModelScope.launch {
            _searchParameter.update {
                it.copy(itemName = itemName, entpName = null)
            }
        }
    }

    /**
     * 제조사명으로 검색
     */
    fun searchMedicinesByEntpName(entpName: String) {
        viewModelScope.launch {
            _searchParameter.update {
                it.copy(itemName = null, entpName = entpName)
            }
        }
    }


    /**
     * 의약품 유형으로 검색
     */
    fun searchMedicinesByMedicationType(medicationType: MedicationType) {
        viewModelScope.launch {
            _searchParameter.update {
                it.copy(medicationType = medicationType)
            }
        }
    }


}