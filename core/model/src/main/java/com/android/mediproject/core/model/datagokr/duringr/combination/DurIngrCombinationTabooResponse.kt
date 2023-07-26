package com.android.mediproject.core.model.datagokr.duringr.combination


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurIngrCombinationTabooResponse(
    @SerialName("body") val body: Body = Body(),

    ) : DataGoKrBaseResponse() {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item> = listOf(),
        @SerialName("numOfRows") val numOfRows: Int = 0, // 15
        @SerialName("pageNo") val pageNo: Int = 0, // 1
        @SerialName("totalCount") val totalCount: Int = 0, // 15
    ) {
        @Serializable
        data class Item(
            @SerialName("item") val item: Item = Item(),
        ) {
            /**
             * @param typeName DUR유형
             * @param mixType 복합제구분(단일/복합)
             * @param ingrCode DUR성분코드
             * @param ingrKorName DUR성분명
             * @param ingrEngName DUR성분 영문명
             * @param className 약효분류
             * @param notificationDate 고시일자
             * @param prohibitContent 금기내용
             * @param remark 비고
             * @param delYn 상태(정상/삭제)
             * @param mix 복합제
             * @param mixtureIngrEngName 병용금기 DUR성분 영문
             * @param mixtureIngrKorName 병용금기 DUR성분 한글
             * @param mixtureClass 병용금기 약효분류
             * @param mixtureIngrCode 병용금기 DUR성분코드
             * @param mixtureMix 병용금기 복합제
             * @param mixtureMixType 병용금기 복합제구분(단일/복합)
             * @param mixtureOri 병용금기 관계성분
             * @param ori 관계성분
             */
            @Serializable
            data class Item(
                @SerialName("CLASS") val className: String = "", // [06290]기타의 화학요법제
                @SerialName("DEL_YN") val delYn: String = "", // 정상
                @SerialName("INGR_CODE") val ingrCode: String = "", // D000762
                @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Itraconazole
                @SerialName("INGR_KOR_NAME") val ingrKorName: String = "", // 이트라코나졸
                @SerialName("MIX") val mix: String = "",
                @SerialName("MIXTURE_CLASS") val mixtureClass: String = "", // [02180]동맥경화용제
                @SerialName("MIXTURE_INGR_CODE") val mixtureIngrCode: String = "", // D000027
                @SerialName("MIXTURE_INGR_ENG_NAME") val mixtureIngrEngName: String = "", // Simvastatin
                @SerialName("MIXTURE_INGR_KOR_NAME") val mixtureIngrKorName: String = "", // 심바스타틴
                @SerialName("MIXTURE_MIX") val mixtureMix: String = "",
                @SerialName("MIXTURE_MIX_TYPE") val mixtureMixType: String = "", // 단일
                @SerialName("MIXTURE_ORI") val mixtureOri: String = "", // [M089710]심바스타틴
                @SerialName("MIX_TYPE") val mixType: String = "", // 단일
                @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20090303
                @SerialName("ORI")
                val ori: String = "", // [M083733]이트라코나졸제피과립/[M083734]이트라코나졸/[M092870]이트라코나졸고체분산체/[M201487]이트라코나졸고체분산/[M201624]제피이트라코나졸과립
                @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 횡문근융해증
                @SerialName("REMARK") val remark: String = "", // 75세 이상 남성
                @SerialName("TYPE_NAME") val typeName: String = "", // 병용금기
            )
        }
    }

}
