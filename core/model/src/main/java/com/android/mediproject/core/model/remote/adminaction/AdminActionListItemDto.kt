package com.android.mediproject.core.model.remote.adminaction

/**
 * 행정처분 목록 별 데이터 클래스
 *
 * @property aDDR 소재지
 * @property aDMDISPSNAME 처분사항
 * @property aDMDISPSSEQ 행정처분 일련번호
 * @property bEFAPPLYLAW 근거법령
 * @property bIZRNO 사업자등록번호
 * @property eNTPNAME 업체명
 * @property iTEMSEQ 품목 일련번호
 * @property iTEMNAME 품목명
 * @property eNTPNO 업체번호
 * @property lASTSETTLEDATE 처분 일자
 * @property rLSENDDATE 공개 종료 일자
 * @property eXPOSECONT 위반내용
 *
 */
data class AdminActionListItemDto(
    val aDDR: String?, // 경기도 화성시 향남읍 제약공단3길27
    val aDMDISPSNAME: String?, // ○ 의약품 ‘아빌리파이정2밀리그램(아리피프라졸)’<제28호>에 대하여 해당 품목 제조업무정지 1개월(2023. 5. 12.~2023. 6. 11.)
    val aDMDISPSSEQ: String?, // 2023003342
    val bEFAPPLYLAW: String?, // ○「약사법」제38조제1항,「의약품 등의 안전에 관한 규칙」제48조제15호,「의약품 소량포장단위 공급에 관한 규정(식품의약품안전처 고시)」 ○「약사법」제76조제1항제3호 및 제3항,「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] “행정처분의 기준” Ⅱ. 개별기준 제25호마목
    val bIZRNO: String?, // 2208114465
    val eNTPNAME: String?, // 한국오츠카제약(주)
    val eNTPNO: String?, // 19830009
    val eXPOSECONT: String?, //  ○ 2021년도 의약품 소량포장단위 공급기준 미준수
    val iTEMNAME: String?, // 아빌리파이정2밀리그램(아리피프라졸)
    val iTEMSEQ: String?, // 200808451
    val lASTSETTLEDATE: String?, // 20230526
    val rLSENDDATE: String? // 20230910
)

fun AdminActionListResponse.Body.Item.toDto(): AdminActionListItemDto {
    return AdminActionListItemDto(
        aDDR = aDDR,
        aDMDISPSNAME = aDMDISPSNAME,
        aDMDISPSSEQ = aDMDISPSSEQ,
        bEFAPPLYLAW = bEFAPPLYLAW,
        bIZRNO = bIZRNO,
        eNTPNAME = eNTPNAME,
        eNTPNO = eNTPNO,
        eXPOSECONT = eXPOSECONT,
        iTEMNAME = iTEMNAME,
        iTEMSEQ = iTEMSEQ,
        lASTSETTLEDATE = lASTSETTLEDATE,
        rLSENDDATE = rLSENDDATE
    )
}