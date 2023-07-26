package com.android.mediproject.core.model.datagokr.durproduct.pregnancy


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductPregnantWomanTabooResponse : DataGoKrResponse<DurProductPregnantWomanTabooResponse.Item>() {

    /**
     * @param changeDate 변경일자
     * @param chart 성상
     * @param classCode 약효분류코드
     * @param className 약효분류
     * @param entpName 업체명
     * @param etcOtcName 전문일반 구분명
     * @param formName 제형명
     * @param ingrCode 성분코드
     * @param ingrEngName 성분영문명
     * @param ingrEngNameFull 성분영문명(전체)
     * @param ingrName 성분명
     * @param itemName 제품명
     * @param itemPermitDate 품목허가일자
     * @param itemSeq 품목기준코드
     * @param mainIngr 주성분
     * @param mixIngr 복합제
     * @param mixType 복합제구분(단일/복합)
     * @param notificationDate 고시일자
     * @param prohibitContent 금기내용
     * @param remark 비고
     * @param typeName DUR유형
     */
    @Serializable
    data class Item(
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 20200327
        @SerialName("CHART") val chart: String = "", // 흰색의 원형 정제
        @SerialName("CLASS_CODE") val classCode: String = "", // 02170
        @SerialName("CLASS_NAME") val className: String = "", // 혈관확장제
        @SerialName("ENTP_NAME") val entpName: String = "", // 에이치케이이노엔(주)
        @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
        @SerialName("FORM_NAME") val formName: String = "", // 나정
        @SerialName("INGR_CODE") val ingrCode: String = "", // D000818
        @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Diltiazem
        @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Diltiazem(딜티아젬)
        @SerialName("INGR_NAME") val ingrName: String = "", // 딜티아젬
        @SerialName("ITEM_NAME") val itemName: String = "", // 헤르벤정(딜티아젬염산염)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19810720
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 198100012
        @SerialName("MAIN_INGR") val mainIngr: String = "", // [M222982]딜티아젬염산염
        @SerialName("MIX_INGR") val mixIngr: String = "", // null
        @SerialName("MIX_TYPE") val mixType: String = "", // 단일
        @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20081211
        @SerialName("PROHBT_CONTENT")
        val prohibitContent: String = "", // "(경구) 동물실험에서 골격, 심장, 망막 및 혀에 기형 보고.출생자의 체중감소 및 생존수 감소, 분만지연, 사산수 증가 보고.(주사)동물 실험에서 최기형성 및 태자치사 작용 보고."
        @SerialName("REMARK") val remark: String = "", // null
        @SerialName("TYPE_NAME") val typeName: String = "", // 임부금기
    )
}
