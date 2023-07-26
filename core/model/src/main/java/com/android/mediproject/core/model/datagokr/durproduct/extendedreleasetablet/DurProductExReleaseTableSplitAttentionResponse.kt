package com.android.mediproject.core.model.datagokr.durproduct.extendedreleasetablet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurProductExReleaseTableSplitAttentionResponse(
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
         * @param bizrNo 사업자등록번호
         * @param changeDate 변경일자
         * @param chart 성상
         * @param classCode 약효분류코드
         * @param className 약효분류
         * @param entpName 업체명
         * @param etcOtcName 전문일반 구분명
         * @param formCodeName 제형코드명
         * @param itemName 제품명
         * @param itemPermitDate 품목허가일자
         * @param itemSeq 품목기준코드
         * @param mainIngr 주성분
         * @param mix 복합제 구분(단일/복합)
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param typeName DUR유형
         */
        @Serializable
        data class Item(
            @SerialName("BIZRNO") val bizrNo: String = "", // 2188100518
            @SerialName("CHANGE_DATE") val changeDate: String = "", // 20210629
            @SerialName("CHART") val chart: String = "", // 내수용 : 연녹색의 원형 장용성 필름코팅정, 수출용 : 적색의 원형 장용성 필름코팅정
            @SerialName("CLASS_CODE") val classCode: String = "", // 03950
            @SerialName("CLASS_NAME") val className: String = "", // 효소제제
            @SerialName("ENTP_NAME") val entpName: String = "", // (주)에이프로젠바이오로직스
            @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 일반의약품
            @SerialName("FORM_CODE_NAME") val formCodeName: String = "", // 장용성필름코팅정
            @SerialName("ITEM_NAME") val itemName: String = "", // 키모랄에스정
            @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 1971May6th
            @SerialName("ITEM_SEQ") val itemSeq: String = "", // 197100081
            @SerialName("MAIN_INGR") val mainIngr: String = "", // [M051649]결정트립신/[M095415]브로멜라인/[M095415]브로멜라인/[M051649]결정트립신
            @SerialName("MIX") val mix: String = "", // 복합
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 분할불가
            @SerialName("REMARK") val remark: String = "", // null
            @SerialName("TYPE_NAME") val typeName: String = "", // 분할주의
        )
    }

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String = "", // 00
        @SerialName("resultMsg") val resultMsg: String = "", // NORMAL SERVICE.
    )
}
