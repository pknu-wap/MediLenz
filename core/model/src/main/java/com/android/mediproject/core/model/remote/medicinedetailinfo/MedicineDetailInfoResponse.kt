package com.android.mediproject.core.model.remote.medicinedetailinfo


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
         * @param aTCCODE ATC 코드
         * @param bARCODE 바코드
         * @param bIZRNO 사업자등록번호
         * @param cANCELDATE 취소일자
         * @param cANCELNAME 취소명
         * @param cHANGEDATE 변경일자
         * @param cHART 성상
         * @param cNSGNMANUF 제조및수입사
         * @param dOCTEXT 효능효과
         * @param eDICODE EDI코드
         * @param eEDOCDATA 의약품 효능효과
         * @param eEDOCID: 의약품 효능효과 문서의 식별자(ID)입니다.
         * @param eNTPENGNAME: 제조사의 영문 이름입니다.
         * @param eNTPNAME: 제조사의 이름입니다.
         * @param eNTPNO: 제조사의 번호입니다.
         * @param eTCOTCCODE: 전문의약품 코드입니다.
         * @param gBNNAME: GBN(General Batch Number)의 이름입니다.
         * @param iNDUTYTYPE: 산업 유형입니다. 이 경우에는 '의약품'이라고 명시되어 있습니다.
         *  @param iNGRNAME: 성분 이름입니다.
         *  @param iNSERTFILE: 삽입 파일의 URL입니다.
         *  @param iTEMENGNAME: 제품의 영문 이름입니다.
         *  @param iTEMNAME: 제품 이름입니다.
         *  @param iTEMPERMITDATE: 제품 허가 날짜입니다.
         *  @param iTEMSEQ: 제품 시퀀스 번호입니다.
         *  @param mAININGRENG: 주성분의 영문 이름입니다.
         *  @param mAINITEMINGR: 주성분입니다.
         *  @param mAKEMATERIALFLAG: 제조재료 플래그입니다. 완제의약품을 나타냅니다.
         *  @param mATERIALNAME: 재료 이름입니다.
         *  @param nARCOTICKINDCODE: 마약 종류 코드입니다.
         *  @param nBDOCDATA: NB 문서 데이터입니다.
         *  @param nBDOCID: NB 문서의 식별자(ID)입니다.
         *  @param nEWDRUGCLASSNAME: 새로운 약물 분류 이름입니다.
         *  @param pACKUNIT: 패키지 단위입니다.
         *  @param pERMITKINDNAME: 허가 종류 이름입니다.
         *  @param pNDOCDATA: PN 문서 데이터입니다.
         *  @param rEEXAMDATE: 재심사 날짜입니다.
         *  @param rEEXAMTARGET: 재심사 대상입니다.
         *  @param sTORAGEMETHOD: 저장 방법입니다.
         *  @param tOTALCONTENT: 총 함량입니다.
         *  @param uDDOCDATA: UD 문서 데이터입니다.
         *  @param uDDOCID: UD 문서의 식별자(ID)입니다.
         *  @param vALIDTERM: 유효 기간입니다. 제조일로부터의 개월 수를 나타냅니다.
         *
         */
        @Serializable
        data class Item(
            @SerialName("ATC_CODE") val aTccode: String?, // A08AA62
            @SerialName("BAR_CODE") val barCode: String?, // 8806418060603,8806418060610,8806418060627,8806418060634
            @SerialName("BIZRNO") val bIZRNO: String?, // 1138108888
            @SerialName("CANCEL_DATE") val cANCELDATE: String?, // null
            @SerialName("CANCEL_NAME") val cANCELNAME: String?, // 정상
            @SerialName("CHANGE_DATE") val cHANGEDATE: String?, // 20220910
            @SerialName("CHART") val cHART: String?, // 파란색 원형의 서방성 필름코팅정
            @SerialName("CNSGN_MANUF") val cNSGNMANUF: String?, // null
            @SerialName("DOC_TEXT") val dOCTEXT: String?,
            @SerialName("EDI_CODE") val eDICODE: String?, // null
            @SerialName("EE_DOC_DATA")
            val eEDOCDATA: String?, // <DOC title="효능효과" type="EE">  <SECTION title="">    <ARTICLE title="">      <PARAGRAPH tagName="p" textIndent="0" marginLeft="0"><![CDATA[체질량지수(BMI) 30kg/m<sup>2</sup> 이상의 비만환자 또는 다른 위험인자(예. 제2형 당뇨, 이상지질혈증, 고혈압)가 있는 체질량지수(BMI) 27kg/m<sup>2</sup> 이상 30kg/m<sup>2</sup> 미만인 과체중 환자의 체중조절을 위한 식이 및 운동요법의 보조요법]]></PARAGRAPH>    </ARTICLE>  </SECTION></DOC>
            @SerialName("EE_DOC_ID") val eEDOCID: String?, // https://nedrug.mfds.go.kr/pbp/cmn/pdfDownload/201602430/EE
            @SerialName("ENTP_ENG_NAME") val eNTPENGNAME: String?, // Kwangdong Pharm Co., Ltd.
            @SerialName("ENTP_NAME") val eNTPNAME: String?, // 광동제약(주)
            @SerialName("ENTP_NO") val eNTPNO: String?, // 0150
            @SerialName("ETC_OTC_CODE") val eTCOTCCODE: String?, // 전문의약품
            @SerialName("GBN_NAME") val gBNNAME: String?,
            @SerialName("INDUTY_TYPE") val iNDUTYTYPE: String?, // 의약품
            @SerialName("INGR_NAME") val iNGRNAME: String?,
            @SerialName("INSERT_FILE") val iNSERTFILE: String?, // https://nedrug.mfds.go.kr/pbp/cmn/pdfDownload/201602430/II
            @SerialName("ITEM_ENG_NAME") val iTEMENGNAME: String?, // null
            @SerialName("ITEM_NAME") val iTEMNAME: String?, // 콘트라브서방정
            @SerialName("ITEM_PERMIT_DATE") val iTEMPERMITDATE: String?, // 20160429
            @SerialName("ITEM_SEQ") val iTEMSEQ: String?, // 201602430
            @SerialName("MAIN_INGR_ENG") val mAININGRENG: String?, // Bupropion Hydrochloride/Naltrexone Hydrochloride
            @SerialName("MAIN_ITEM_INGR") val mAINITEMINGR: String?, // [M233238]부프로피온염산염|[M254702]날트렉손염산염
            @SerialName("MAKE_MATERIAL_FLAG") val mAKEMATERIALFLAG: String?, // 완제의약품
            @SerialName("MATERIAL_NAME") val mATERIALNAME: String?,
            @SerialName("NARCOTIC_KIND_CODE") val nARCOTICKINDCODE: String?, // null
            @SerialName("NB_DOC_DATA") val nBDOCDATA: String?,
            @SerialName("NB_DOC_ID") val nBDOCID: String?, // https://nedrug.mfds.go.kr/pbp/cmn/pdfDownload/201602430/NB
            @SerialName("NEWDRUG_CLASS_NAME") val nEWDRUGCLASSNAME: String?,
            @SerialName("PACK_UNIT") val pACKUNIT: String?,
            @SerialName("PERMIT_KIND_NAME") val pERMITKINDNAME: String?, // 허가
            @SerialName("PN_DOC_DATA") val pNDOCDATA: String?, // null
            @SerialName("REEXAM_DATE") val rEEXAMDATE: String?, // null
            @SerialName("REEXAM_TARGET") val rEEXAMTARGET: String?, // null
            @SerialName("STORAGE_METHOD") val sTORAGEMETHOD: String?, // 기밀용기, 25℃ 이하 보관
            @SerialName("TOTAL_CONTENT") val tOTALCONTENT: String?, // 1680-부프로피온염산염층/1680-중간층/1680-날트렉손염산염층/1680-코팅층
            @SerialName("UD_DOC_DATA") val uDDOCDATA: String?,
            @SerialName("UD_DOC_ID") val uDDOCID: String?, // https://nedrug.mfds.go.kr/pbp/cmn/pdfDownload/201602430/UD
            @SerialName("VALID_TERM") val vALIDTERM: String? // 제조일로부터 36 개월
        )
    }

}