package com.android.mediproject.core.model.adminaction


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 행정처분 목록 조회 응답
 *
 * @property body
 * @property header
 */
@Serializable
class AdminActionListResponse : DataGoKrResponse<AdminActionListResponse.Item>() {

    /**
     * 행정처분 목록 별 데이터 클래스
     *
     * @property address 소재지
     * @property aDMDISPSNAME 처분사항
     * @property aDMDISPSSEQ 행정처분 일련번호
     * @property bEFAPPLYLAW 근거법령
     * @property bizrNo 사업자등록번호
     * @property entpName 업체명
     * @property itemSeq 품목 일련번호
     * @property itemName 품목명
     * @property eNTPNO 업체번호
     * @property lASTSETTLEDATE 처분 일자
     * @property rLSENDDATE 공개 종료 일자
     * @property eXPOSECONT 위반내용
     *
     */
    @Serializable
    data class Item(
        @SerialName("ADDR") val address: String, // 경기도 화성시 향남읍 제약공단3길27
        @SerialName("ADM_DISPS_NAME") val aDMDISPSNAME: String, // ○ 의약품 ‘아빌리파이정2밀리그램(아리피프라졸)’<제28호>에 대하여 해당 품목 제조업무정지 1개월(2023. 5. 12.~2023. 6. 11.)
        @SerialName("ADM_DISPS_SEQ") val aDMDISPSSEQ: String, // 2023003342
        @SerialName("BEF_APPLY_LAW")
        val bEFAPPLYLAW: String, // ○「약사법」제38조제1항,「의약품 등의 안전에 관한 규칙」제48조제15호,「의약품 소량포장단위 공급에 관한 규정(식품의약품안전처 고시)」 ○「약사법」제76조제1항제3호 및 제3항,「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] “행정처분의 기준” Ⅱ. 개별기준 제25호마목
        @SerialName("BIZRNO") val bizrNo: String?, // 2208114465
        @SerialName("ENTP_NAME") val entpName: String, // 한국오츠카제약(주)
        @SerialName("ENTP_NO") val eNTPNO: String, // 19830009
        @SerialName("EXPOSE_CONT") val eXPOSECONT: String, //  ○ 2021년도 의약품 소량포장단위 공급기준 미준수
        @SerialName("ITEM_NAME") val itemName: String?, // 아빌리파이정2밀리그램(아리피프라졸)
        @SerialName("ITEM_SEQ") val itemSeq: String?, // 200808451
        @SerialName("LAST_SETTLE_DATE") val lASTSETTLEDATE: String, // 20230526
        @SerialName("RLS_END_DATE") val rLSENDDATE: String, // 20230910
    )


}
