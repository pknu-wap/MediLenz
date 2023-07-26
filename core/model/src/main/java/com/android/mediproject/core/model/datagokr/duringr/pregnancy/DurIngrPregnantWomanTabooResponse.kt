package com.android.mediproject.core.model.datagokr.duringr.pregnancy


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurIngrPregnantWomanTabooResponse : DataGoKrResponse<DurIngrPregnantWomanTabooResponse.Item>() {
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
         * @param grade 등급
         * @param notificationDate 고시일자
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param delYn 상태(정상/삭제)
         */
        @Serializable
        data class Item(
            @SerialName("CLASS_NAME") val className: String = "", // [02470]난포호르몬제 및 황체호르몬제
            @SerialName("DEL_YN") val delYn: String = "", // 정상
            @SerialName("DUR_SEQ") val durSeq: String = "", // 1893
            @SerialName("FORM_NAME")
            val formName: String = "", // 연질캡슐제, 현탁상/유화주사제/용액용분말주사제/서방성현탁액용분말주사제/용액용동결건조분말주사제/현탁액용동결건조분말주사제/서방성현탁액용동결건조분말주사제/리포좀화현탁액용동결건조분말주사제/현탁액주사제/서방성현탁액성주사제/용액주사제
            @SerialName("GRADE") val grade: String = "", // 1등급
            @SerialName("INGR_CODE") val ingrCode: String = "", // D000100
            @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Progesterone
            @SerialName("INGR_NAME") val ingrKorName: String = "", // 프로게스테론
            @SerialName("MIX_INGR") val mixIngr: String = "",
            @SerialName("MIX_TYPE") val mixType: String = "", // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20081211
            @SerialName("ORI_INGR") val oriIngr: String = "", // [M040719]프로게스테론/[M086429]미분화프로게스테론/[M252921]프로게스테론(미분화)
            @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // 임부에 대한 안전성 미확립.
            @SerialName("REMARK") val remark: String = "", // 경구
            @SerialName("TYPE_NAME") val typeName: String = "", // 임부금기
        )
    }

}
