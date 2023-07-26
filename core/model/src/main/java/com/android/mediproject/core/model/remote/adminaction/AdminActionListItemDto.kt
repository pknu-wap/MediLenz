package com.android.mediproject.core.model.remote.adminaction


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
/**
 * 행정처분 목록 별 데이터 클래스
 *
 * @property address 소재지
 * @property disposition 처분사항
 * @property enforcementNumber 행정처분 일련번호
 * @property applyLaw 근거법령
 * @property bizrNo 사업자등록번호
 * @property entpName 업체명
 * @property itemSeq 품목 일련번호
 * @property itemName 품목명
 * @property entpNo 업체번호
 * @property lastSettleDate 처분 일자
 * @property publicEndDate 공개 종료 일자
 * @property violation 위반 내용
 *
 */
data class AdminActionListItemDto(
    val address: String, // 경기도 화성시 향남읍 제약공단3길27
    val disposition: String, // ○ 의약품 ‘아빌리파이정2밀리그램(아리피프라졸)’<제28호>에 대하여 해당 품목 제조업무정지 1개월(2023. 5. 12.~2023. 6. 11.)
    val enforcementNumber: String, // 2023003342
    val applyLaw: String, // ○「약사법」제38조제1항,「의약품 등의 안전에 관한 규칙」제48조제15호,「의약품 소량포장단위 공급에 관한 규정(식품의약품안전처 고시)」 ○「약사법」제76조제1항제3호 및 제3항,「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] “행정처분의 기준” Ⅱ. 개별기준 제25호마목
    val bizrNo: String, // 2208114465
    val entpName: String, // 한국오츠카제약(주)
    val entpNo: String, // 19830009
    val violation: String, //  ○ 2021년도 의약품 소량포장단위 공급기준 미준수
    val itemName: String, // 아빌리파이정2밀리그램(아리피프라졸)
    val itemSeq: String, // 200808451
    val lastSettleDate: LocalDate, // 20230526
    val publicEndDate: LocalDate, // 20230910
    var onClick: (() -> Unit)? = null,
) : Parcelable

fun AdminActionListResponse.Body.Item.toDto(): AdminActionListItemDto {
    return AdminActionListItemDto(
        address = aDDR,
        disposition = aDMDISPSNAME,
        enforcementNumber = aDMDISPSSEQ,
        applyLaw = bEFAPPLYLAW,
        bizrNo = bizrNo ?: "",
        entpName = entpName,
        entpNo = eNTPNO,
        violation = eXPOSECONT,
        itemName = itemName ?: "",
        itemSeq = itemSeq ?: "",
        lastSettleDate = LocalDate.parse(lASTSETTLEDATE, dateFormatter),
        publicEndDate = LocalDate.parse(rLSENDDATE, dateFormatter),

        )
}

private val dateFormatter by lazy { DateTimeFormatter.ofPattern("yyyyMMdd") }
