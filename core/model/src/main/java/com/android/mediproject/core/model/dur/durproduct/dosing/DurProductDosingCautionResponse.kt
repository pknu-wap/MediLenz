package com.android.mediproject.core.model.dur.durproduct.dosing


import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.servercommon.NetworkApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductDosingCautionResponse : DataGoKrResponse<DurProductDosingCautionResponse.Item>() {

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
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 20140103
        @SerialName("CHART") val chart: String = "", // 흰색의 원형 필름코팅정
        @SerialName("CLASS_CODE") val classCode: String = "", // 02390
        @SerialName("CLASS_NAME") val className: String = "", // 기타의 소화기관용약
        @SerialName("ENTP_NAME") val entpName: String = "", // 동화약품(주)
        @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
        @SerialName("FORM_NAME") val formName: String = "", // 필름코팅정
        @SerialName("INGR_CODE") val ingrCode: String = "", // D000425
        @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Metoclopramide
        @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Metoclopramide(메토클로프라미드)
        @SerialName("INGR_NAME") val ingrName: String = "", // 메토클로프라미드
        @SerialName("ITEM_NAME") val itemName: String = "", // 맥페란정(메토클로프라미드)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19720210
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 197200484
        @SerialName("MAIN_INGR") val mainIngr: String = "", // [M050465]메토클로프라미드
        @SerialName("MIX_INGR") val mixIngr: String = "", // null
        @SerialName("MIX_TYPE") val mixType: String = "", // 단일
        @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20150331
        @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // null
        @SerialName("REMARK") val remark: String = "", // null
        @SerialName("TYPE_NAME") val typeName: String = "", // 투여기간주의
    ) : NetworkApiResponse.ListItem


}
