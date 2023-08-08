package com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication


import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.servercommon.NetworkApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductEfficacyGroupDuplicationResponse : DataGoKrResponse<DurProductEfficacyGroupDuplicationResponse.Item>() {

    /**
     * @param changeDate 변경일자
     * @param chart 성상
     * @param classCode 약효분류코드
     * @param entpName 업체명
     * @param etcOtcName 전문일반 구분명
     * @param formName 제형명
     * @param ingrCode 성분코드
     * @param ingrEngName 성분영문명
     * @param ingrName 성분명
     * @param itemName 제품명
     * @param itemPermitDate 품목허가일자
     * @param itemSeq 품목기준코드
     * @param mainIngr 주성분
     * @param mixIngr 복합제
     * @param bizrNo 사업자등록번호
     * @param className 약효분류
     * @param durSeq DUR 일련번호
     * @param etcOtcCode 전문일반 구분코드
     * @param formCode 제형코드
     * @param ingrEngNameFull 성분영문명(전체)
     * @param effectName 효능
     * @param mix 복합제 구분(단일/복합)
     * @param formCodeName 제형코드명
     * @param notificationDate 고시일자
     * @param prohibitContent 금기내용
     * @param remark 비고
     * @param typeName DUR유형
     */
    @Serializable
    data class Item(
        @SerialName("BIZRNO") val bizrNo: String = "", // 1138106691
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 20131231
        @SerialName("CHART") val chart: String = "", // 백색의 원형정제이다.
        @SerialName("CLASS_CODE") val classCode: String = "", // 01190
        @SerialName("CLASS_NAME") val className: String = "", // 기타의 중추신경용약
        @SerialName("DUR_SEQ") val durSeq: String = "", // 2600
        @SerialName("EFFECT_NAME") val effectName: String = "", // 정신신경용제
        @SerialName("ENTP_NAME") val entpName: String = "", // 아주약품(주)
        @SerialName("ETC_OTC_CODE") val etcOtcCode: String = "", // 02
        @SerialName("ETC_OTC_NAME") val etcOtcName: String = "", // 전문의약품
        @SerialName("FORM_CODE") val formCode: String = "", // 010101
        @SerialName("FORM_CODE_NAME") val formCodeName: String = "", // 나정
        @SerialName("FORM_NAME") val formName: String = "", // 나정
        @SerialName("INGR_CODE") val ingrCode: String = "", // D000468
        @SerialName("INGR_ENG_NAME") val ingrEngName: String = "", // Orphenadrine Hydrochloride
        @SerialName("INGR_ENG_NAME_FULL") val ingrEngNameFull: String = "", // Orphenadrine(오르페나드린)
        @SerialName("INGR_NAME") val ingrName: String = "", // 오르페나드린염산염
        @SerialName("ITEM_NAME") val itemName: String = "", // 닉신정(오르페나드린염산염)(수출용)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 19841019
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 198400314
        @SerialName("MAIN_INGR") val mainIngr: String = "", // [M222877]오르페나드린염산염
        @SerialName("MIX") val mix: String = "", // 단일
        @SerialName("MIX_INGR") val mixIngr: String = "", // null
        @SerialName("NOTIFICATION_DATE") val notificationDate: String = "", // 20131227
        @SerialName("PROHBT_CONTENT") val prohibitContent: String = "", // null
        @SerialName("REMARK") val remark: String = "", // null
        @SerialName("TYPE_NAME") val typeName: String = "", // 효능군중복
    ) : NetworkApiResponse.ListItem
}
