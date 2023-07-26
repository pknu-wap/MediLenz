package com.android.mediproject.core.model.datagokr.duringr


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurIngrSpecialtyAgeGroupTabooInfoResponse(
    @SerialName("body") val body: Body = Body(),
    @SerialName("header") val header: Header = Header(),
) {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0, // 15
        @SerialName("pageNo") val pageNo: Int = 0, // 1
        @SerialName("totalCount") val totalCount: Int = 0, // 1
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
             * @param className 약효분류
             * @param ageBase 나이대
             * @param formName 제형
             * @param notificationDate 고시일자
             * @param prohibitContent 금기내용
             * @param remark 비고
             * @param delYn 상태(정상/삭제)
             */
            @Serializable
            data class Item(
                @SerialName("AGE_BASE") val ageBase: String = "", // 18세 이하
                @SerialName("CLASS_NAME") val className: String = "", // [03960]당뇨병용제
                @SerialName("DEL_YN") val delYn: String = "", // 정상
                @SerialName("DUR_SEQ") val durSeq: String = "", // 455
                @SerialName("FORM_NAME") val formName: String = "", // 정제
                @SerialName("INGR_CODE") val ingrCode: String = "", // D000149
                @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Acarbose
                @SerialName("INGR_NAME") val ingrKorName: String = "", // 아카보즈
                @SerialName("MIX_INGR") val mixIngr: String = "",
                @SerialName("MIX_TYPE") val mixType: String = "", // 단일
                @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20140109
                @SerialName("ORI_INGR") val oriIngr: String = "", // [M085039]아카보즈
                @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 안전성 및 유효성 미확립
                @SerialName("REMARK") val remark: String = "", // null
                @SerialName("TYPE_NAME") val typeName: String = "", // 특정연령대금기
            )
        }
    }

    @Serializable
    data class Header(
        @SerialName("resultCode") val resultCode: String = "", // 00
        @SerialName("resultMsg") val resultMsg: String = "", // NORMAL SERVICE.
    )
}
