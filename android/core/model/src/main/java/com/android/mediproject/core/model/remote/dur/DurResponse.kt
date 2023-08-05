package com.android.mediproject.core.model.remote.dur


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DurResponse(
    @SerialName("body") val body: Body,
) : DataGoKrBaseResponse() {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item>, @SerialName("numOfRows") val numOfRows: Int, // 15
        @SerialName("pageNo") val pageNo: Int, // 1
        @SerialName("totalCount") val totalCount: Int // 1
    ) {
        /**
         * @param barCode 바코드
         * @param bizRno 사업자등록번호
         * @param cancelDate 취소일
         * @param cancelName 취소명
         * @param changeDate 변경일
         * @param chart 차트
         * @param classNo 클래스 번호
         * @param ediCode EDI 코드
         * @param eeDocId EE 문서 ID
         * @param entpName 기업 이름
         * @param etcOtcCode 기타 OTC 코드
         * @param insertFile 삽입 파일
         * @param itemName 아이템 이름
         * @param itemPermitDate 아이템 허가 날짜
         * @param itemSeq 아이템 순서
         * @param materialName 재료 이름
         * @param nbDocId NB 문서 ID
         * @param packUnit 패키지 단위
         * @param reexamDate 재검사 날짜
         * @param reexamTarget 재검사 대상
         * @param storageMethod 저장 방법
         * @param typeCode 유형 코드
         * @param typeName 유형 이름
         * @param udDocId UD 문서 ID
         * @param validTerm 유효 기간
         */
        @Serializable
        data class Item(
            @SerialName("BAR_CODE") val barCode: String?, // 8806421025729
            @SerialName("BIZRNO") val bizRno: String?, // 1188100601
            @SerialName("CANCEL_DATE") val cancelDate: String?, // null
            @SerialName("CANCEL_NAME") val cancelName: String?, // 정상
            @SerialName("CHANGE_DATE") val changeDate: String?, // 2019August26th
            @SerialName("CHART") val chart: String?, // 미황색의 원형 정제
            @SerialName("CLASS_NO") val classNo: String?, // [141]항히스타민제
            @SerialName("EDI_CODE") val ediCode: String?, // 642102570
            @SerialName("EE_DOC_ID") val eeDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/EE
            @SerialName("ENTP_NAME") val entpName: String?, // (주)유한양행
            @SerialName("ETC_OTC_CODE") val etcOtcCode: String?, // 일반의약품
            @SerialName("INSERT_FILE") val insertFile: String?, // HTTP://WWW.HEALTH.KR/IMAGES/INSERT_PDF/In_A11A0450A0085_00.pdf
            @SerialName("ITEM_NAME") val itemName: String?, // 페니라민정(클로르페니라민말레산염)
            @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String?, // 1960October10th
            @SerialName("ITEM_SEQ") val itemSeq: String?, // 196000011
            @SerialName("MATERIAL_NAME") val materialName: String?, // 클로르페니라민말레산염,,2.0,밀리그램,KP,
            @SerialName("NB_DOC_ID") val nbDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/NB
            @SerialName("PACK_UNIT") val packUnit: String?, // 1000정/병
            @SerialName("REEXAM_DATE") val reexamDate: String?, // null
            @SerialName("REEXAM_TARGET") val reexamTarget: String?, // null
            @SerialName("STORAGE_METHOD") val storageMethod: String?, // 실온, 건소, 밀폐용기,
            @SerialName("TYPE_CODE") val typeCode: String?, // D,F,I
            @SerialName("TYPE_NAME  ") val typeName: String?, // 용량주의,노인주의,첨가제주의
            @SerialName("UD_DOC_ID") val udDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/UD
            @SerialName("VALID_TERM") val validTerm: String? // 제조일로부터 36 개월
        )
    }

}