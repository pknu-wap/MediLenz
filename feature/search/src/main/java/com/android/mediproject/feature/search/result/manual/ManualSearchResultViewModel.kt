package com.android.mediproject.feature.search.result.manual

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.constants.MedicationType
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.model.requestparameters.ApprovalListSearchParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
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
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _searchParameter = MutableStateFlow(
        ApprovalListSearchParameter(
            itemName = null, entpName = null, medicationType = MedicationType.ALL
        )
    )
    val searchParameter = _searchParameter.asStateFlow()

    val searchResultFlow: StateFlow<UiState<PagingData<ApprovedMedicineItemDto>>> by lazy {
        searchParameter.flatMapLatest { parameter ->
            if (parameter.entpName.isNullOrEmpty() && parameter.itemName.isNullOrEmpty()) {
                flowOf(UiState.Error("검색어를 입력해주세요."))
            } else {
                getMedicineApprovalListUseCase(parameter).cachedIn(viewModelScope).flatMapLatest {
                    flowOf(UiState.Success(it))
                }
            }
        }.flowOn(ioDispatcher).catch {
            flowOf(UiState.Error(it.message ?: "알 수 없는 오류가 발생했습니다."))
        }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = UiState.Loading)
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