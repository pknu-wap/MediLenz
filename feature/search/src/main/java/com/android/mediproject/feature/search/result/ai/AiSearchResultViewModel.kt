package com.android.mediproject.feature.search.result.ai

import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.bindingadapter.ISendEvent
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.MutableEventFlow
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.asEventFlow
import com.android.mediproject.core.domain.GetMedicineDetailsUseCase
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.core.model.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetail
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.search.result.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiSearchResultViewModel @Inject constructor(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ISendEvent<MedicineDetail> {

    private val _classificationResult = MutableStateFlow<List<ClassificationResult>>(emptyList())

    val classificationResult = _classificationResult.asStateFlow()

    private val _eventState = MutableEventFlow<EventState>(replay = 1)

    val eventState = _eventState.asEventFlow()


    val medicineList: StateFlow<UiState<List<ClassificationResult>>> = _classificationResult.flatMapLatest { classificationList ->
        val itemSeqs = classificationList.map {
            it.classificationRecognition.medicineSeq
        }
        getMedicineDetailsUseCase.getMedicineDetailInfoByItemSeq(itemSeqs).flatMapLatest { response ->
            response.fold(
                onSuccess = { medicineDetailInfoList ->
                    medicineDetailInfoList.mapIndexed { index, medicineDetailInfo ->
                        classificationList[index].medicineDetail = medicineDetailInfo
                        classificationList[index].onClick = ::send
                    }.let {
                        flowOf(UiState.Success(classificationList))
                    }
                },
                onFailure = {
                    flowOf(UiState.Error(it.message ?: "약품 상세 정보 조회에 실패했습니다."))
                },
            )
        }
    }.flowOn(defaultDispatcher).stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = UiState.Loading)

    fun setClassificationResult(classificationResult: List<ClassificationResult>) {
        viewModelScope.launch {
            this@AiSearchResultViewModel._classificationResult.value = classificationResult
        }
    }

    override fun send(e: MedicineDetail) {
        viewModelScope.launch(defaultDispatcher) {
            _eventState.emit(
                EventState.OpenMedicineInfo(
                    MedicineInfoArgs(
                        entpKorName = e.entpName,
                        entpEngName = e.entpEnglishName,
                        itemIngrName = e.mainItemIngredient,
                        itemKorName = e.itemName,
                        itemEngName = e.itemEnglishName,
                        itemSeq = e.itemSequence.toLong(),
                        productType = e.industryType,
                        medicineType = e.etcOtcCode,
                        imgUrl = e.insertFileUrl,
                    ),
                ),
            )
        }
    }

}
