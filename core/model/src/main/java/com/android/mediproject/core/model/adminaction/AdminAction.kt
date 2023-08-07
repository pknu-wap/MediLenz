package com.android.mediproject.core.model.adminaction


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
/**
 * 행정처분 목록 별 데이터 클래스
 *
 * @property companyAddress 업체 소재지
 * @property adminAction 행정처분명
 * @property adminActionNumber 행정처분 일련번호
 * @property violationLaw 위반법령
 * @property companyBizrNo 사업자등록번호
 * @property companyName 업체명
 * @property itemSeq 품목기준코드
 * @property itemName 품목명
 * @property companyRegistrationNumber 업체 일련번호
 * @property adminActionDate 행정처분일자(최종확정일자)
 * @property disclosureEndDate 공개 종료 일자
 * @property violationDetails 위반 내용
 *
 */
data class AdminAction(
    val companyAddress: String, // 경기도 화성시 향남읍 제약공단3길27
    val adminAction: String, // ○ 의약품 ‘아빌리파이정2밀리그램(아리피프라졸)’<제28호>에 대하여 해당 품목 제조업무정지 1개월(2023. 5. 12.~2023. 6. 11.)
    val adminActionNumber: String, // 2023003342
    val violationLaw: String, // ○「약사법」제38조제1항,「의약품 등의 안전에 관한 규칙」제48조제15호,「의약품 소량포장단위 공급에 관한 규정(식품의약품안전처 고시)」 ○「약사법」제76조제1항제3호 및 제3항,「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] “행정처분의 기준” Ⅱ. 개별기준 제25호마목
    val companyBizrNo: String, // 2208114465
    val companyName: String, // 한국오츠카제약(주)
    val companyRegistrationNumber: String, // 19830009
    val violationDetails: String, //  ○ 2021년도 의약품 소량포장단위 공급기준 미준수
    val itemName: String, // 아빌리파이정2밀리그램(아리피프라졸)
    val itemSeq: String, // 200808451
    val adminActionDate: LocalDate, // 20230526
    val disclosureEndDate: LocalDate, // 20230910
    var onClick: (() -> Unit)? = null,
) : Parcelable

fun AdminActionListResponse.Item.toAdminAction(): AdminAction {
    return AdminAction(
        companyAddress = address,
        adminAction = aDMDISPSNAME,
        adminActionNumber = aDMDISPSSEQ,
        violationLaw = bEFAPPLYLAW,
        companyBizrNo = bizrNo ?: "",
        companyName = entpName,
        companyRegistrationNumber = eNTPNO,
        violationDetails = eXPOSECONT,
        itemName = itemName ?: "",
        itemSeq = itemSeq ?: "",
        adminActionDate = LocalDate.parse(lASTSETTLEDATE, dateFormatter),
        disclosureEndDate = LocalDate.parse(rLSENDDATE, dateFormatter),

        )
}

private val dateFormatter by lazy { DateTimeFormatter.ofPattern("yyyyMMdd") }
