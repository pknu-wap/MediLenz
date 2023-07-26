package com.android.mediproject.core.model.datagokr.durproduct.capacity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurProductCapacityAttentionResponse(
    @SerialName("body") val body: Body = Body(),
    @SerialName("header") val header: Header = Header(),
) {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0, // 3
        @SerialName("pageNo") val pageNo: Int = 0, // 1
        @SerialName("totalCount") val totalCount: Int = 0, // 1
    ) {
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
         *
         */
        @Serializable
        data class Item(
            @SerialName("CHANGE_DATE") val changeDate: String = "", // 20190826
            @SerialName("CHART") val chart: String = "", // 미황색의 원형 정제
            @SerialName("CLASS_CODE") val classCode: String = "", // 01410
            @SerialName("CLASS_NAME") val className: String = "", // 항히스타민제
            @SerialName("ENTP_NAME") val entpName: String = "", // (주)유한양행
            @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 일반의약품
            @SerialName("FORM_NAME") val formName: String = "", // 나정
            @SerialName("INGR_CODE") val ingrCode: String = "", // D000893
            @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Chlorpheniramine Maleate
            @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Chlorpheniramine Maleate(클로르페니라민말레산염)
            @SerialName("INGR_NAME") val ingrName: String = "", // 클로르페니라민말레산염
            @SerialName("ITEM_NAME") val itemName: String = "", // 페니라민정(클로르페니라민말레산염)
            @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19601010
            @SerialName("ITEM_SEQ") val itemSeq: String = "", // 196000011
            @SerialName("MAIN_INGR") val mainIngr: String = "", // [M223211]클로르페니라민말레산염
            @SerialName("MIX_INGR") val mixIngr: String = "", // null
            @SerialName("MIX_TYPE") val mixType: String = "", // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20180831
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 클로르페니라민말레산염 24mg
            @SerialName("REMARK") val remark: String = "", // null
            @SerialName("TYPE_NAME") val typeName: String = "", // 용량주의
        )
    }

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String = "", // 00
        @SerialName("resultMsg") val resultMsg: String = "", // NORMAL SERVICE.
    )
}
