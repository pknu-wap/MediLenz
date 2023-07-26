package com.android.mediproject.feature.search.result.manual

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import asEventFlow
import com.android.mediproject.core.common.bindingadapter.ISendEvent
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.constants.MedicationType
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.model.requestparameters.ApprovalListSearchParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.search.result.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
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
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ISendEvent<ApprovedMedicineItemDto> {

    private val _searchParameter =
        MutableStateFlow(ApprovalListSearchParameter(itemName = null, entpName = null, medicationType = MedicationType.ALL))
    val searchParameter = _searchParameter.asStateFlow()

    val searchResultFlow = searchParameter.flatMapLatest {
        getMedicineApprovalListUseCase(it).cachedIn(viewModelScope).mapLatest { pagingData ->
            pagingData.map { item ->
                item.onClick = ::send
                item
            }
        }
    }

    private val _eventState = MutableEventFlow<EventState>(replay = 1)

    val eventState = _eventState.asEventFlow()


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

    override fun send(e: ApprovedMedicineItemDto) {
        viewModelScope.launch(defaultDispatcher) {
            _eventState.emit(
                EventState.OpenMedicineInfo(
                    MedicineInfoArgs(
                        entpKorName = e.entpName,
                        entpEngName = e.entpEngName ?: "",
                        itemIngrName = e.itemIngrName,
                        itemKorName = e.itemName,
                        itemEngName = e.itemEngName ?: "",
                        itemSeq = e.itemSeq,
                        productType = e.prductType,
                        medicineType = e.medicineType,
                        imgUrl = e.imgUrl ?: "",
                    ),
                ),
            )
        }
    }
}
