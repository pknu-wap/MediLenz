package com.android.mediproject.core.model.dur.durproduct.specialtyagegroup


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductSpecialtyAgeGroupTabooResponse : DataGoKrResponse<DurProductSpecialtyAgeGroupTabooResponse.Item>() {

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
     */
    @Serializable
    data class Item(
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 20200313
        @SerialName("CHART") val chart: String = "", // 황색의 결정 또는 결정성 가루가 들어 있는 상부는 갈색, 하부는 담회색의 캅셀이다.
        @SerialName("CLASS_CODE") val classCode: String = "", // 06150
        @SerialName("CLASS_NAME") val className: String = "", // 주로 그람양성, 음성균, 리케치아, 비루스에 작용하는 것
        @SerialName("ENTP_NAME") val entpName: String = "", // (주)종근당
        @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
        @SerialName("FORM_NAME") val formName: String = "", // 경질캡슐제, 산제
        @SerialName("INGR_CODE") val ingrCode: String = "", // D000064
        @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Tetracycline Hydrochloride
        @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Tetracycline Hydrochloride(테트라사이클린염산염)
        @SerialName("INGR_NAME") val ingrName: String = "", // 테트라사이클린염산염
        @SerialName("ITEM_NAME") val itemName: String = "", // 테라싸이클린캅셀250밀리그람(염산테트라싸이클린)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19600614
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 196000001
        @SerialName("MAIN_INGR") val mainIngr: String = "", // [M223235]테트라사이클린염산염
        @SerialName("MIX_INGR") val mixIngr: String = "", // null
        @SerialName("MIX_TYPE") val mixType: String = "", // 단일
        @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20140109
        @SerialName("PROHBT_CONTENT")
        val prohibitContent: String = "", // 소아 등(특히 치아 형성기인 12세 미만의 소아)에 투여 시, 치아의 착색？법랑질 형성 부전, 또는 일과성 골발육 부전을 일으킬 수 있음
        @SerialName("REMARK") val remark: String = "", // 다만, 다른 약을 사용할 수 없거나 효과가 없는 경우에만 8세 이상 신중투여
        @SerialName("TYPE_NAME") val typeName: String = "", // 특정연령대금기
    ) : LeafItem

}
