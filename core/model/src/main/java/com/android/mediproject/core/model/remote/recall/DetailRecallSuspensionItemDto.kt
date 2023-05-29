package com.android.mediproject.core.model.remote.recall

import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * 상세 리콜 폐기 데이터
 *
 * @property bIZRNO 사업자등록번호
 * @property enterprise 업체명
 * @property enterpriseAddress 업체 주소
 * @property enterpriseTel 업체 전화번호
 * @property manufacturedDate 제조일자
 * @property manufactureNo 제조번호
 * @property openEndDate 공개 마감 일자
 * @property packageUnit 포장단위
 * @property product 제품명
 * @property recallCommandDate 회수명령일자
 * @property rm 비고
 * @property approvalDate 승인일자
 * @property reasonForRecall  회수사유
 * @property usagePeriod 유효기한
 *
 */
data class DetailRecallSuspensionItemDto(
    val bIZRNO: String, // 1278641862
    val enterprise: String, // (주)자연세상
    val enterpriseAddress: String, // 경기도 포천시 소흘읍 호국로481번길 21-16
    val enterpriseTel: String, // 0315443091
    val manufacturedDate: LocalDate?, // 20211001
    val manufactureNo: String, // 330-061-21
    val openEndDate: LocalDate, // 20250922
    val packageUnit: String, // 자사포장단위
    val product: String, // 자연세상갈근
    val recallCommandDate: LocalDate, // 20220923
    val rm: String?, // 비고
    val approvalDate: LocalDate?, // 20221004000000
    val reasonForRecall: String, // 정량법 부적합
    val usagePeriod: String? // 제조일로부터36개월
)

fun DetailRecallSuspensionResponse.Body.Item.Item.toDto(): DetailRecallSuspensionItemDto = DetailRecallSuspensionItemDto(
    bIZRNO = bIZRNO ?: "",
    enterprise = eNTRPS ?: "",
    enterpriseAddress = eNTRPSADRES ?: "",
    enterpriseTel = eNTRPSTELNO ?: "",
    manufacturedDate = manufacturedDate?.run { LocalDate.parse(this, dateFormat) },
    manufactureNo = manufactureId ?: "",
    openEndDate = LocalDate.parse(openEndDate, dateFormat),
    packageUnit = pACKNGUNIT ?: "",
    product = pRDUCT ?: "",
    recallCommandDate = recalLCommandDate.run { LocalDate.parse(this, dateFormat) },
    rm = rM,
    approvalDate = null,
    reasonForRecall = retrievalReason ?: "",
    usagePeriod = usagePeriod
)

private val dateFormat by lazy { DateTimeFormatter.ofPattern("yyyyMMdd") }