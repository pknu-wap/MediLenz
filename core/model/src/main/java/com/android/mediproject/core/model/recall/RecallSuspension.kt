package com.android.mediproject.core.model.recall


import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.toLocalDate


/**
 * 리콜 폐기 목록 별 데이터 클래스
 *
 * @property enfrcYn 강제여부
 * @property entrps 업체명
 * @property itemSeq 일련번호
 * @property product 제품명
 * @property recallCommandDate 리콜 명령 일자
 * @property destructionOrderDate 폐기 명령 일자
 * @property reason 폐기 사유
 *
 */
data class RecallSuspension(
    val enfrcYn: String?, // N
    val entrps: String, // 동국제약(주)
    val itemSeq: String, // 201208461
    val product: String, // 니자틴캡슐(니자티딘)
    val recallCommandDate: DateTimeValue, // 20230504
    val destructionOrderDate: DateTimeValue, // 20230503000000
    val reason: String, // 안정성 시험결과 NDMA 기준 초과에 따른 사전예방적 조치로 시중 유통품에 대해 영업자회수
    var onClick: ((RecallSuspension) -> Unit)? = null,
)

fun RecallSuspensionListResponse.Item.Item.toRecallSuspension() = RecallSuspension(
    enfrcYn = enfrcYn,
    entrps = entrps ?: "",
    itemSeq = itemSeq ?: "",
    product = product ?: "",
    recallCommandDate = recallCommandDate.toLocalDate("yyyyMMdd"),
    destructionOrderDate = rtrlCommandDt.toLocalDate("yyyyMMddHHmmss"),
    reason = rtrvlResn ?: "",
)
