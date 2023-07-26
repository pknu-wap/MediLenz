package com.android.mediproject.core.model.dur.durproduct.senior


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductSeniorCautionResponse : DataGoKrResponse<DurProductSeniorCautionResponse.Item>() {


    /**
     * @param changeDate 변경일자
     * @param chart 성상
     * @param entpName 업체명
     * @param itemName 제품명
     * @param itemPermitDate 품목허가일자
     * @param itemSeq 품목기준코드
     * @param typeName DUR 유형
     * @param className 약효분류
     * @param classCode 약효분류코드
     * @param etcOtcName 전문일반 구분명
     * @param formName 제형명
     * @param ingrCode 성분코드
     * @param ingrEngName 성분영문명
     * @param ingrName 성분명
     * @param mainIngr 주성분
     * @param mixIngr 복합제
     * @param mixType 복합제구분(단일/복합)
     * @param notificationDate 고시일자
     * @param prohibitContent 금기내용
     * @param remark 비고
     * @param ingrEngNameFull 성분영문명(전체)
     */
    @Serializable
    data class Item(
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 20221201
        @SerialName("CHART") val chart: String = "", // 청색의 원형 필름코팅정
        @SerialName("CLASS_CODE") val classCode: String = "", // 01170
        @SerialName("CLASS_NAME") val className: String = "", // 정신신경용제
        @SerialName("ENTP_NAME") val entpName: String = "", // 환인제약(주)
        @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
        @SerialName("FORM_NAME") val formName: String = "", // 필름코팅정
        @SerialName("INGR_CODE") val ingrCode: String = "", // D000809
        @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Amitriptyline
        @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Amitriptyline(아미트리프틸린)
        @SerialName("INGR_NAME") val ingrName: String = "", // 아미트리프틸린
        @SerialName("ITEM_NAME") val itemName: String = "", // 에나폰정10밀리그램(아미트리프틸린염산염)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19700220
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 197000079
        @SerialName("MAIN_INGR") val mainIngr: String = "", // [M223101]아미트리프틸린염산염
        @SerialName("MIX_INGR") val mixIngr: String = "", // [M223101]아미트리프틸린염산염
        @SerialName("MIX_TYPE") val mixType: String = "", // 단일
        @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20150728
        @SerialName("PROHBT_CONTENT")
        val prohibitContent: String = "", // 노인에서의 삼환계 항우울제 사용은 기립성 저혈압, 비틀거림, 항콜린작용에 의한 구갈, 배뇨곤란, 변비, 안내압항진 등이 나타나기 쉬움으로 소량으로 신중투여
        @SerialName("REMARK") val remark: String = "", // null
        @SerialName("TYPE_NAME") val typeName: String = "", // 노인주의
    )


}
