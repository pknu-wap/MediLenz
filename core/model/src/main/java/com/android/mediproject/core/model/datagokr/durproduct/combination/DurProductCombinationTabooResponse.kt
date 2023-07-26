package com.android.mediproject.core.model.datagokr.durproduct.combination


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurProductCombinationTabooResponse(
    @SerialName("body") val body: Body = Body(),
) : DataGoKrBaseResponse() {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0, // 15
        @SerialName("pageNo") val pageNo: Int = 0, // 1
        @SerialName("totalCount") val totalCount: Int = 0, // 5
    ) {
        /**
         * @param bizrNo 사업자등록번호
         * @param changeDate 변경일자
         * @param chart 성상
         * @param classCode 약효분류코드
         * @param className 약효분류
         * @param durSeq DUR 일련번호
         * @param entpName 업체명
         * @param etcOtcCode 전문일반 구분코드
         * @param etcOtcName 전문일반 구분명
         * @param formCode 제형코드
         * @param formName 제형명
         * @param ingrCode 성분코드
         * @param ingrEngName 성분영문명
         * @param ingrKorName 성분명
         * @param itemName 제품명
         * @param itemPermitDate 품목허가일자
         * @param itemSeq 품목기준코드
         * @param mainIngr 주성분
         * @param mix 복합제 구분(단일/복합)
         * @param mixIngr 복합제
         * @param mixtureChangeDate 병용변경일자
         * @param mixtureChart 병용금기 성상
         * @param mixtureClassCode 병용금기 분류코드
         * @param mixtureClassName 병용금기 분류명
         * @param mixtureDurSeq 병용금기 DUR번호
         * @param mixtureEntpName 병용금기 업체명
         * @param mixtureEtcOtcCode 병용금기 전문일반구분코드
         * @param mixtureEtcOtcName 병용금기 전문일반구분명
         * @param mixtureFormCode 병용금기 제형코드
         * @param mixtureFormName 병용금기 제형명
         * @param mixtureIngrCode 병용금기 성분코드
         * @param mixtureIngrEngName 병용금기 성분영문명
         * @param mixtureIngrKorName 병용금기 성분명
         * @param mixtureItemName 병용금기 제품명
         * @param mixtureItemPermitDate 병용금기 허가일자
         * @param mixtureItemSeq 병용금기 품목기준코드
         * @param mixtureMainIngr 병용금기 주성분
         * @param mixtureMix 병용금기 복합제구분
         * @param notificationDate 고시일자
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param typeCode 금기 유형코드
         * @param typeName 금기 유형명
         */
        @Serializable
        data class Item(
            @SerialName("BIZRNO") val bizrNo: String = "", // 1188100450
            @SerialName("CHANGE_DATE") val changeDate: String = "", // null
            @SerialName("CHART") val chart: String = "", // 무색 맑은 용액이 충전된 무색의 앰플 주사제
            @SerialName("CLASS_CODE") val classCode: String = "", // 01120
            @SerialName("CLASS_NAME") val className: String = "", // 최면진정제
            @SerialName("DUR_SEQ") val durSeq: String = "", // 705
            @SerialName("ENTP_NAME") val entpName: String = "", // 부광약품(주)
            @SerialName("ETC_OTC_CODE") val etcOtcCode: String = "", // 02
            @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
            @SerialName("FORM_CODE") val formCode: String = "", // 210101
            @SerialName("FORM_NAME") val formName: String = "", // 용액주사제
            @SerialName("INGR_CODE") val ingrCode: String = "", // D000007
            @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Midazolam
            @SerialName("INGR_KOR_NAME") val ingrKorName: String = "", // 미다졸람
            @SerialName("ITEM_NAME") val itemName: String = "", // 부광미다졸람주사15밀리그램/3밀리리터(수출용)
            @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 20200413
            @SerialName("ITEM_SEQ") val itemSeq: String = "", // 202002585
            @SerialName("MAIN_INGR") val mainIngr: String = "", // [M222760]미다졸람
            @SerialName("MIX") val mix: String = "", // 단일
            @SerialName("MIX_INGR") val mixIngr: String = "", // null
            @SerialName("MIXTURE_CHANGE_DATE") val mixtureChangeDate: String = "", // 20230530
            @SerialName("MIXTURE_CHART") val mixtureChart: String = "", // 흰색의 장방형 필름코팅정
            @SerialName("MIXTURE_CLASS_CODE") val mixtureClassCode: String = "", // 06290
            @SerialName("MIXTURE_CLASS_NAME") val mixtureClassName: String = "", // 기타의 화학요법제
            @SerialName("MIXTURE_DUR_SEQ") val mixtureDurSeq: String = "", // 705
            @SerialName("MIXTURE_ENTP_NAME") val mixtureEntpName: String = "", // 한국애브비(주)
            @SerialName("MIXTURE_ETC_OTC_CODE") val mixtureEtcOtcCode: String = "", // 02
            @SerialName("MIXTURE_ETC_OTC_NAME") val mixtureEtcOtcName: String = "", // 전문의약품
            @SerialName("MIXTURE_FORM_CODE") val mixtureFormCode: String = "", // 010201
            @SerialName("MIXTURE_FORM_NAME") val mixtureFormName: String = "", // 필름코팅정
            @SerialName("MIXTURE_INGR_CODE") val mixtureIngrCode: String = "", // D000712
            @SerialName("MIXTURE_INGR_ENG_NAME") val mixtureIngrEngName: String = "", // Ritonavir
            @SerialName("MIXTURE_INGR_KOR_NAME") val mixtureIngrKorName: String = "", // 리토나비르
            @SerialName("MIXTURE_ITEM_NAME") val mixtureItemName: String = "", // 노비르정(리토나비르)
            @SerialName("MIXTURE_ITEM_PERMIT_DATE") val mixtureItemPermitDate: String = "", // 20110901
            @SerialName("MIXTURE_ITEM_SEQ") val mixtureItemSeq: String = "", // 201106064
            @SerialName("MIXTURE_MAIN_INGR") val mixtureMainIngr: String = "", // [M259362]리토나비르
            @SerialName("MIXTURE_MIX") val mixtureMix: String = "", // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20091203
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 진정작용의 연장 또는 증가, 호흡저하
            @SerialName("REMARK") val remark: String = "", // null
            @SerialName("TYPE_CODE") val typeCode: String = "", // A
            @SerialName("TYPE_NAME") val typeName: String = "", // 병용금기
        )
    }


}
