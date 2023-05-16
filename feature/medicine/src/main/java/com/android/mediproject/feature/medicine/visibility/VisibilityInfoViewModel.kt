package com.android.mediproject.feature.medicine.visibility

import android.content.Context
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetGranuleIdentificationUseCase
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.medicine.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisibilityInfoViewModel @Inject constructor(
    private val getGranuleIdentificationUseCase: GetGranuleIdentificationUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _granuleIdentification = MutableStateFlow<UiState<GranuleIdentificationInfoDto>>(UiState.Loading)
    val granuleIdentification get() = _granuleIdentification.asStateFlow()

    private val _granuleTextTags = MutableStateFlow<Spanned>(Html.fromHtml("", Html.FROM_HTML_MODE_COMPACT))
    val granuleTextTags get() = _granuleTextTags.asStateFlow()

    fun getGranuleIdentificationInfo(
        itemName: String?, entpName: String?, itemSeq: String?
    ) = viewModelScope.launch {
        _granuleIdentification.value = UiState.Loading
        getGranuleIdentificationUseCase(itemName, entpName, itemSeq).fold(onSuccess = {
            _granuleIdentification.value = UiState.Success(it)
        }, onFailure = { _granuleIdentification.value = UiState.Error(it.message ?: "failed") })
    }


    suspend fun createDataTags(context: Context) {
        viewModelScope.launch(ioDispatcher) {

            granuleIdentification.collectLatest { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        uiState.data.also { granuleDto ->
                            var dataMap: Map<String, List<Pair<String, String>>>? =
                                mutableMapOf<String, List<Pair<String, String>>>().apply {
                                    // 그룹 1: 의약품 정보
                                    context.resources.getStringArray(R.array.medicineInfo).also {
                                        this[context.getString(R.string.medicineInfoTitle)] = listOf(
                                            it[0] to granuleDto.itemSeq, it[1] to granuleDto.itemName, it[2] to granuleDto.itemEngName
                                        )
                                    }

                                    // 그룹 2: 업체 정보
                                    context.resources.getStringArray(R.array.companyInfo).also {
                                        this[context.getString(R.string.companyInfoTitle)] = listOf(
                                            it[0] to granuleDto.entpSeq, it[1] to granuleDto.entpName, it[2] to granuleDto.bizrNo
                                        )
                                    }

                                    // 그룹 3: 의약품 분류 정보
                                    context.resources.getStringArray(R.array.classificationInfo).also {
                                        this[context.getString(R.string.classificationInfoTitle)] = listOf(
                                            it[0] to granuleDto.classNo, it[1] to granuleDto.className, it[2] to granuleDto.etcOtcName
                                        )
                                    }

                                    // 그룹 4: 기타 정보
                                    context.resources.getStringArray(R.array.miscInfo).also {
                                        this[context.getString(R.string.miscInfoTitle)] = listOf(
                                            it[0] to granuleDto.formCodeName,
                                            it[1] to granuleDto.itemPermitDate.toString(),
                                            it[2] to granuleDto.changeDate.toString()
                                        )
                                    }

                                    // 그룹 5: 인쇄 및 이미지 정보
                                    context.resources.getStringArray(R.array.printAndImageInfo).also {
                                        this[context.getString(R.string.printAndImageInfoTitle)] = listOf(
                                            it[0] to (granuleDto.printFront ?: "없음"),
                                            it[1] to (granuleDto.printBack ?: "없음"),
                                            it[2] to granuleDto.markCodeFrontAnal,
                                            it[3] to granuleDto.markCodeBackAnal,
                                            it[4] to granuleDto.markCodeFrontImg,
                                            it[5] to granuleDto.markCodeBackImg
                                        )
                                    }

                                    // 그룹 6: 의약품 이미지 정보
                                    context.resources.getStringArray(R.array.medicineImageInfo).also {
                                        this[context.getString(R.string.medicineImageInfoTitle)] = listOf(
                                            it[0] to granuleDto.chart,
                                            it[1] to granuleDto.itemImage,
                                        )
                                    }

                                    // 그룹 7: 의약품 형태 정보
                                    context.resources.getStringArray(R.array.medicineShapeInfo).also {
                                        this[context.getString(R.string.medicineShapeInfoTitle)] = listOf(
                                            it[0] to granuleDto.drugShape,
                                            it[1] to granuleDto.colorClass1,
                                            it[2] to (granuleDto.colorClass2 ?: "없음"),
                                        )
                                    }

                                    // 그룹 8: 구분선 정보
                                    context.resources.getStringArray(R.array.lineInfo).also {
                                        this[context.getString(R.string.lineInfoTitle)] = listOf(
                                            it[0] to (granuleDto.lineFront ?: "없음"),
                                            it[1] to (granuleDto.lineBack ?: "없음"),
                                            it[2] to granuleDto.lengLong,
                                            it[3] to granuleDto.lengShort,
                                            it[4] to (granuleDto.thick ?: "없음"),
                                            it[5] to granuleDto.imgRegistTs.toString(),
                                        )
                                    }
                                }.toMap().also { dataMap ->
                                    var builder: StringBuilder? = StringBuilder().also { builder ->
                                        dataMap.forEach { (groupTitle, items) ->
                                            builder.append("<h2>").append(groupTitle).append("</h2>")
                                            items.forEach { pair ->
                                                builder.append("<p><b>").append(pair.first).append(": </b>").append(pair.second)
                                                    .append("</p>")
                                            }
                                            builder.append("<br>")
                                        }
                                        _granuleTextTags.value = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
                                    }

                                    builder?.clear()
                                    builder = null
                                }

                            dataMap = null
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}