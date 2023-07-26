package com.android.mediproject.core.model.datagokr.duringr.capacity


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurIngrCapacityAttentionResponse : DataGoKrResponse<DurIngrCapacityAttentionResponse.Item>() {

    @Serializable
    data class Item(
        @SerialName("item") val item: Item = Item(),
    ) {

        /**
         * @param durSeq DUR번호
         * @param typeName DUR유형
         * @param mixType 복합제구분(단일/복합)
         * @param ingrCode DUR성분코드
         * @param ingrName DUR성분명
         * @param ingrEngName DUR성분 영문명
         * @param mixIngr 복합제
         * @param oriIngr 관계성분
         * @param className 약효분류
         * @param formName 제형
         * @param maxQty 1일 최대용량
         * @param notificationDate 고시일자
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param delYn 상태(정상/삭제)
         */
        @Serializable
        data class Item(
            @SerialName("CLASS_NAME") val className: String = "", // [01120]최면진정제
            @SerialName("DEL_YN") val delYn: String = "", // 정상
            @SerialName("DUR_SEQ") val durSeq: String = "", // 636
            @SerialName("FORM_NAME") val formName: String = "", // 정제
            @SerialName("INGR_CODE") val ingrCode: String = "", // D000592
            @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Triazolam
            @SerialName("INGR_NAME") val ingrName: String = "", // 트리아졸람
            @SerialName("MAX_QTY") val maxQty: String = "", // 0.25밀리그램
            @SerialName("MIX_INGR") val mixIngr: String = "", // 복합제
            @SerialName("MIX_TYPE") val mixType: String = "", // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20130703
            @SerialName("ORI_INGR") val oriIngr: String = "", // [I005603]트리아졸람/[M088380]트리아졸람
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 금기내용
            @SerialName("REMARK") val remark: String = "", // 비고
            @SerialName("TYPE_NAME") val typeName: String = "", // 용량주의
        )
    }

}
