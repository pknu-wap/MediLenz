package com.android.mediproject.core.model.datagokr.duringr.senior


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurIngrSeniorCautionResponse(
    @SerialName("body") val body: Body = Body(),
    @SerialName("header") val header: Header = Header(),
) {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0, // 15
        @SerialName("pageNo") val pageNo: Int = 0, // 1
        @SerialName("totalCount") val totalCount: Int = 0, // 2
    ) {
        @Serializable
        data class Item(
            @SerialName("item") val item: Item = Item(),
        ) {

            /**
             * @param durSeq DUR번호
             * @param typeName DUR유형
             * @param mixType 복합제구분(단일/복합)
             * @param ingrCode DUR성분코드
             * @param ingrKorName DUR성분 명
             * @param ingrEngName DUR성분 영문명
             * @param mixIngr 복합제
             * @param oriIngr 관계성분
             * @param formName 제형
             * @param notificationDate 고시일자
             * @param prohibitContent 금기내용
             * @param remark 비고
             * @param delYn 상태(정상/삭제)
             */
            @Serializable
            data class Item(
                @SerialName("DEL_YN") val delYn: String = "", // 정상
                @SerialName("DUR_SEQ") val durSeq: String = "", // 1
                @SerialName("FORM_NAME") val formName: String = "",
                @SerialName("INGR_CODE") val ingrCode: String = "", // D000056
                @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Chlordiazepoxide
                @SerialName("INGR_NAME") val ingrKorName: String = "", // 클로르디아제폭시드
                @SerialName("MIX_INGR") val mixIngr: String = "",
                @SerialName("MIX_TYPE") val mixType: String = "", // 단일
                @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20150728
                @SerialName("ORI_INGR") val oriIngr: String = "", // [M088403]클로르디아제폭시드/[M223206]클로르디아제폭시드염산염
                @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 노인에서의 장기지속형 벤조다이아제핀 사용은 운동실조, 과진정 등이 나타나기 쉬움으로 소량부터 신중투여
                @SerialName("REMARK") val remark: String = "", // null
                @SerialName("TYPE_NAME") val typeName: String = "", // 노인주의
            )
        }
    }

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String = "", // 00
        @SerialName("resultMsg") val resultMsg: String = "", // NORMAL SERVICE.
    )
}
