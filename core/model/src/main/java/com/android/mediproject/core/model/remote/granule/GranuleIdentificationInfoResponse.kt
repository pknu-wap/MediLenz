package com.android.mediproject.core.model.remote.granule


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GranuleIdentificationInfoResponse : DataGoKrResponse<GranuleIdentificationInfoResponse.Item>() {

    /**
     * 식별 정보
     *
     * @param bizrNo 사업자등록번호
     * @param changeDate 변경일자
     * @param chart 모양
     * @param className 분류명
     * @param classNo 분류번호
     * @param colorClass1 색상1
     * @param colorClass2 색상2
     * @param drugShape 의약품 모양
     * @param ediCode EDI코드
     * @param entpName 업체명
     * @param entpSeq 업체일련번호
     * @param etcOtcName 구분
     * @param formCodeName 제형코드
     * @param imgRegistTs 이미지 등록 일시
     * @param itemEngName 제품영문명
     * @param itemName 제품명
     * @param itemSeq 품목일련번호
     * @param itemImage 이미지
     * @param itemPermitDate 허가일자
     * @param lengLong 의약품 길이
     * @param lengShort 의약품 폭
     * @param lineBack 선
     * @param lineFront 선
     * @param markCodeBackImg 마크코드(뒷면)
     * @param markCodeBack 마크코드(뒷면)
     * @param markCodeBackAnal 마크코드(앞면)
     * @param markCodeFront 마크코드(앞면)
     * @param markCodeFrontImg 마크코드(앞면)
     * @param markCodeFrontAnal 마크코드(앞면)
     * @param printBack 인쇄(뒷면)
     * @param printFront 인쇄(앞면)
     * @param thick 의약품 두께
     *
     */
    @Serializable
    data class Item(
        @SerialName("BIZRNO") val bizrNo: String, // 3038117108
        @SerialName("CHANGE_DATE") val changeDate: String, // 20230419
        @SerialName("CHART") val chart: String, // 무색 내지 연보라색의 내용물이 든 보라색의 투명한 타원형 연질캡슐
        @SerialName("CLASS_NAME") val className: String, // 항히스타민제
        @SerialName("CLASS_NO") val classNo: String, // 01410
        @SerialName("COLOR_CLASS1") val colorClass1: String, // 보라, 투명
        @SerialName("COLOR_CLASS2") val colorClass2: String?, // null
        @SerialName("DRUG_SHAPE") val drugShape: String, // 타원형
        @SerialName("EDI_CODE") val ediCode: String?, // null
        @SerialName("ENTP_NAME") val entpName: String, // (주)녹십자
        @SerialName("ENTP_SEQ") val entpSeq: String, // 19910005
        @SerialName("ETC_OTC_NAME") val etcOtcName: String, // 일반의약품
        @SerialName("FORM_CODE_NAME") val formCodeName: String, // 연질캡슐제, 액상
        @SerialName("IMG_REGIST_TS") val imgRegistTs: String, // 20100305
        @SerialName("ITEM_ENG_NAME") val itemEngName: String, // Allershot Soft Cap.
        @SerialName("ITEM_IMAGE") val itemImage: String, // https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154661648918800015
        @SerialName("ITEM_NAME") val itemName: String, // 알러샷연질캡슐(세티리진염산염)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String, // 20090812
        @SerialName("ITEM_SEQ") val itemSeq: String, // 200906254
        @SerialName("LENG_LONG") val lengLong: String, // 12.50
        @SerialName("LENG_SHORT") val lengShort: String, // 7.58
        @SerialName("LINE_BACK") val lineBack: String?, // null
        @SerialName("LINE_FRONT") val lineFront: String?, // null
        @SerialName("MARK_CODE_BACK") val markCodeBack: String?, // null
        @SerialName("MARK_CODE_BACK_ANAL") val markCodeBackAnal: String,
        @SerialName("MARK_CODE_BACK_IMG") val markCodeBackImg: String,
        @SerialName("MARK_CODE_FRONT") val markCodeFront: String?, // null
        @SerialName("MARK_CODE_FRONT_ANAL") val markCodeFrontAnal: String,
        @SerialName("MARK_CODE_FRONT_IMG") val markCodeFrontImg: String,
        @SerialName("PRINT_BACK") val printBack: String?, // null
        @SerialName("PRINT_FRONT") val printFront: String?, // A-S
        @SerialName("THICK") val thick: String?,
    )
}
