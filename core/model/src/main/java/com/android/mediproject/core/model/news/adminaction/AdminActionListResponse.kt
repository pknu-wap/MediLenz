package com.android.mediproject.core.model.news.adminaction


import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.servercommon.NetworkApiResponse
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
     * @property companyAddress 소재지
     * @property adminAction 처분사항
     * @property adminActionNo 행정처분 일련번호
     * @property violationLaw 근거법령
     * @property companyBizrNo 사업자등록번호
     * @property companyName 업체명
     * @property itemSeq 품목 일련번호
     * @property itemName 품목명
     * @property companyRegistrationNumber 업체번호
     * @property adminActionDate 처분 일자
     * @property disclosureEndDate 공개 종료 일자
     * @property violationDetails 위반내용
     *
     */
    @Serializable
    data class Item(
        @SerialName("ADDR") val companyAddress: String, // 경기도 화성시 향남읍 제약공단3길27
        @SerialName("ADM_DISPS_NAME") val adminAction: String, // ○ 의약품 ‘아빌리파이정2밀리그램(아리피프라졸)’<제28호>에 대하여 해당 품목 제조업무정지 1개월(2023. 5. 12.~2023. 6. 11.)
        @SerialName("ADM_DISPS_SEQ") val adminActionNo: String, // 2023003342
        @SerialName("BEF_APPLY_LAW")
        val violationLaw: String, // ○「약사법」제38조제1항,「의약품 등의 안전에 관한 규칙」제48조제15호,「의약품 소량포장단위 공급에 관한 규정(식품의약품안전처 고시)」 ○「약사법」제76조제1항제3호 및 제3항,「의약품 등의 안전에 관한 규칙」제95조 관련 [별표 8] “행정처분의 기준” Ⅱ. 개별기준 제25호마목
        @SerialName("BIZRNO") val companyBizrNo: String?, // 2208114465
        @SerialName("ENTP_NAME") val companyName: String, // 한국오츠카제약(주)
        @SerialName("ENTP_NO") val companyRegistrationNumber: String, // 19830009
        @SerialName("EXPOSE_CONT") val violationDetails: String, //  ○ 2021년도 의약품 소량포장단위 공급기준 미준수
        @SerialName("ITEM_NAME") val itemName: String?, // 아빌리파이정2밀리그램(아리피프라졸)
        @SerialName("ITEM_SEQ") val itemSeq: String?, // 200808451
        @SerialName("LAST_SETTLE_DATE") val adminActionDate: String, // 20230526
        @SerialName("RLS_END_DATE") val disclosureEndDate: String, // 20230910
    ) : NetworkApiResponse.ListItem


}
