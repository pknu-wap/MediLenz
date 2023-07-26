package com.android.mediproject.core.model.datagokr.durproduct.productlist


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductListResponse : DataGoKrResponse<DurProductListResponse.Item>() {


    /**
     * @param barCode 표준코드
     * @param bizrNo 사업자등록번호
     * @param cancelDate 취소일자
     * @param cancelName 취소사유
     * @param changeDate 변경일자
     * @param chart 성상
     * @param classNo 분류번호
     * @param ediCode 보험코드
     * @param eeDocId 제조방법
     * @param entpName 업체명
     * @param etcOtcCode 전문일반 구분코드
     * @param insertFile 첨부파일
     * @param itemName 제품명
     * @param itemPermitDate 품목허가일자
     * @param itemSeq 품목기준코드
     * @param materialName 원료성분
     * @param nbDocId 주의사항
     * @param packUnit 포장단위
     * @param reexamDate 재심사기간
     * @param reexamTarget 재심사대상
     * @param storageMethod 저장방법
     * @param typeCode 유형코드
     * @param typeName DUR 유형
     * @param validTerm 유효기간
     */
    @Serializable
    data class Item(
        @SerialName("BAR_CODE") val barCode: String = "", // 8806421025729
        @SerialName("BIZRNO") val bizrNo: String = "", // 1188100601
        @SerialName("CANCEL_DATE") val cancelDate: String = "", // null
        @SerialName("CANCEL_NAME") val cancelName: String = "", // 정상
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 2019August26th
        @SerialName("CHART") val chart: String = "", // 미황색의 원형 정제
        @SerialName("CLASS_NO") val classNo: String = "", // [141]항히스타민제
        @SerialName("EDI_CODE") val ediCode: String = "", // 642102570
        @SerialName("EE_DOC_ID") val eeDocId: String = "", // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/EE
        @SerialName("ENTP_NAME") val entpName: String = "", // (주)유한양행
        @SerialName("ETC_OTC_CODE") val etcOtcCode: String = "", // 일반의약품
        @SerialName("INSERT_FILE") val insertFile: String = "", // HTTP://WWW.HEALTH.KR/IMAGES/INSERT_PDF/In_A11A0450A0085_00.pdf
        @SerialName("ITEM_NAME") val itemName: String = "", // 페니라민정(클로르페니라민말레산염)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 1960October10th
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 196000011
        @SerialName("MATERIAL_NAME") val materialName: String = "", // 클로르페니라민말레산염,,2.0,밀리그램,KP,
        @SerialName("NB_DOC_ID") val nbDocId: String = "", // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/NB
        @SerialName("PACK_UNIT") val packUnit: String = "", // 1000정/병
        @SerialName("REEXAM_DATE") val reexamDate: String = "", // null
        @SerialName("REEXAM_TARGET") val reexamTarget: String = "", // null
        @SerialName("STORAGE_METHOD") val storageMethod: String = "", // 실온, 건소, 밀폐용기,
        @SerialName("TYPE_CODE") val typeCode: String = "", // D,F,I
        @SerialName("TYPE_NAME  ") val typeName: String = "", // 용량주의,노인주의,첨가제주의
        @SerialName("VALID_TERM") val validTerm: String = "", // 제조일로부터 36 개월
    ) {
        val typeNames = typeName.split(",")
        val typeCodes = typeCode.split(",")
    }
}
