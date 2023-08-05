package com.android.mediproject.core.model.medicine.medicinedetailinfo


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 의약품 상세 허가 정보 응답
 *
 * @param body
 * @param header
 */
@Serializable
data class MedicineDetailInfoResponse(
    @SerialName("body") val body: Body,
) : DataGoKrBaseResponse() {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item>, @SerialName("numOfRows") val numOfRows: Int, // 100
        @SerialName("pageNo") val pageNo: Int, // 1
        @SerialName("totalCount") val totalCount: Int // 1
    ) {
        /**
         * 의약품 상세 허가 정보
         *
         * @param atcCode ATC 코드
         * @param barCode 바코드
         * @param businessRegistrationNumber 사업자등록번호
         * @param cancelDate 취소일자
         * @param cancelName 취소명
         * @param changeDate 변경일자
         * @param chart 성상
         * @param consignmentManufacturer 제조및수입사
         * @param docText 효능효과
         * @param ediCode EDI코드
         * @param eeDocData 의약품 효능효과
         * @param eeDocId 의약품 효능효과 문서의 식별자(ID)입니다.
         * @param entpEnglishName 제조사의 영문 이름입니다.
         * @param entpName 제조사의 이름입니다.
         * @param entpNumber 제조사의 번호입니다.
         * @param etcOtcCode 전문의약품 코드입니다.
         * @param gbnName GBN(General Batch Number)의 이름입니다.
         * @param industryType 산업 유형입니다. 이 경우에는 '의약품'이라고 명시되어 있습니다.
         * @param ingredientName 성분 이름입니다.
         * @param insertFileUrl 삽입 파일의 URL입니다.
         * @param itemEnglishName 제품의 영문 이름입니다.
         * @param itemName 제품 이름입니다.
         * @param itemPermitDate 제품 허가 날짜입니다.
         * @param itemSequence 제품 시퀀스 번호입니다.
         * @param mainIngredientEnglish 주성분의 영문 이름입니다.
         * @param mainItemIngredient 주성분입니다.
         * @param makeMaterialFlag 제조재료 플래그입니다. 완제의약품을 나타냅니다.
         * @param materialName 재료 이름입니다.
         * @param narcoticKindCode 마약 종류 코드입니다.
         * @param nbDocData NB 문서 데이터입니다.
         * @param nbDocId NB 문서의 식별자(ID)입니다.
         * @param newDrugClassName 새로운 약물 분류 이름입니다.
         * @param packUnit 패키지 단위입니다.
         * @param permitKindName 허가 종류 이름입니다.
         * @param pnDocData PN 문서 데이터입니다.
         * @param reexamDate 재심사 날짜입니다.
         * @param reexamTarget 재심사 대상입니다.
         * @param storageMethod 저장 방법입니다.
         * @param totalContent 총 함량입니다.
         * @param udDocData UD 문서 데이터입니다.
         * @param uDDOCID UD 문서의 식별자(ID)입니다.
         * @param validTerm 유효 기간입니다. 제조일로부터의 개월 수를 나타냅니다.
         */
        @Serializable
        data class Item(
            @SerialName("ATC_CODE") val atcCode: String?,
            @SerialName("BAR_CODE") val barCode: String?,
            @SerialName("BIZRNO") val businessRegistrationNumber: String?,
            @SerialName("CANCEL_DATE") val cancelDate: String?,
            @SerialName("CANCEL_NAME") val cancelName: String?,
            @SerialName("CHANGE_DATE") val changeDate: String?,
            @SerialName("CHART") val chart: String?,
            @SerialName("CNSGN_MANUF") val consignmentManufacturer: String?,
            @SerialName("DOC_TEXT") val docText: String?,
            @SerialName("EDI_CODE") val ediCode: String?,
            @SerialName("EE_DOC_DATA") val eeDocData: String,
            @SerialName("EE_DOC_ID") val eeDocId: String?,
            @SerialName("ENTP_ENG_NAME") val entpEnglishName: String?,
            @SerialName("ENTP_NAME") val entpName: String,
            @SerialName("ENTP_NO") val entpNumber: String?,
            @SerialName("ETC_OTC_CODE") val etcOtcCode: String,
            @SerialName("GBN_NAME") val gbnName: String?,
            @SerialName("INDUTY_TYPE") val industryType: String?,
            @SerialName("INGR_NAME") val ingredientName: String,
            @SerialName("INSERT_FILE") val insertFileUrl: String?,
            @SerialName("ITEM_ENG_NAME") val itemEnglishName: String,
            @SerialName("ITEM_NAME") val itemName: String,
            @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String,
            @SerialName("ITEM_SEQ") val itemSequence: String,
            @SerialName("MAIN_INGR_ENG") val mainIngredientEnglish: String?,
            @SerialName("MAIN_ITEM_INGR") val mainItemIngredient: String,
            @SerialName("MAKE_MATERIAL_FLAG") val makeMaterialFlag: String?,
            @SerialName("MATERIAL_NAME") val materialName: String?,
            @SerialName("NARCOTIC_KIND_CODE") val narcoticKindCode: String?,
            @SerialName("NB_DOC_DATA") val nbDocData: String,
            @SerialName("NB_DOC_ID") val nbDocId: String?,
            @SerialName("NEWDRUG_CLASS_NAME") val newDrugClassName: String?,
            @SerialName("PACK_UNIT") val packUnit: String?,
            @SerialName("PERMIT_KIND_NAME") val permitKindName: String?,
            @SerialName("PN_DOC_DATA") val pnDocData: String?,
            @SerialName("REEXAM_DATE") val reexamDate: String?,
            @SerialName("REEXAM_TARGET") val reexamTarget: String?,
            @SerialName("STORAGE_METHOD") val storageMethod: String?,
            @SerialName("TOTAL_CONTENT") val totalContent: String?,
            @SerialName("UD_DOC_DATA") val udDocData: String,
            @SerialName("UD_DOC_ID") val uDDOCID: String?,
            @SerialName("VALID_TERM") val validTerm: String?
        )
    }

}