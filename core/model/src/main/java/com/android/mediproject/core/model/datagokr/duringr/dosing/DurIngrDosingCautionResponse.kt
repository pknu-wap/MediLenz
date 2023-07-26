package com.android.mediproject.core.model.datagokr.duringr.dosing


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurIngrDosingCautionResponse : DataGoKrResponse<DurIngrDosingCautionResponse.Item>() {

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
         * @param formName 제형
         * @param maxDosageTerm 최대 투여 기간
         * @param notificationDate 고시일자
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param delYn 상태(정상/삭제)
         */
        @Serializable
        data class Item(
            @SerialName("CLASS_NAME") val className: String = "", // [01120]최면진정제
            @SerialName("DEL_YN") val delYn: String = "", // 정상
            @SerialName("DUR_SEQ") val durSeq: String = "", // 459
            @SerialName("FORM_NAME") val formName: String = "", // 정제
            @SerialName("INGR_CODE") val ingrCode: String = "", // D000592
            @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Triazolam
            @SerialName("INGR_NAME") val ingrKorName: String = "", // 트리아졸람
            @SerialName("MAX_DOSAGE_TERM") val maxDosageTerm: String = "", // 21일
            @SerialName("MIX_INGR") val mixIngr: String = "",
            @SerialName("MIX_TYPE") val mixType: String = "", // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20100511
            @SerialName("ORI_INGR") val oriIngr: String = "", // [I005603]트리아졸람/[M088380]트리아졸람
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // null
            @SerialName("REMARK") val remark: String = "", // null
            @SerialName("TYPE_NAME") val typeName: String = "", // 투여기간주의
        )
    }

}
