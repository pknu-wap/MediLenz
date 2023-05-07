package com.android.mediproject.core.model.medicine

import kotlinx.serialization.Serializable

/**
 * 의약품 식별 정보(낱알 정보, 형태 등)를 화면에 보여줄때 사용합니다.
 *
 *ITEM_SEQ : 의약품 코드
 *
 *ITEM_NAME : 의약품 이름
 *
 *ENTP_SEQ : 업체 코드
 *
 *ENTP_NAME : 업체 이름
 *
 *CHART : 의약품의 캡슐 형태 및 색상 정보
 *
 *ITEM_IMAGE : 의약품 이미지 URL
 *
 *PRINT_FRONT : 앞면 인쇄 정보
 *
 *PRINT_BACK : 뒷면 인쇄 정보
 *
 *DRUG_SHAPE : 의약품의 형태 정보
 *
 *COLOR_CLASS1 : 색상 정보 1
 *
 *COLOR_CLASS2 : 색상 정보 2
 *
 *LINE_FRONT : 앞면 구분선 정보
 *
 *
 *LINE_BACK : 뒷면 구분선 정보
 *
 *LENG_LONG : 긴 변의 길이
 *
 *LENG_SHORT : 짧은 변의 길이
 *
 *THICK : 두께
 *
 *IMG_REGIST_TS : 이미지 등록 일시
 *
 *CLASS_NO : 의약품 분류 코드
 *
 *CLASS_NAME : 의약품 분류 이름
 *
 *ETC_OTC_NAME : 의약품 유형 정보
 *
 *ITEM_PERMIT_DATE : 의약품 허가 일자
 *
 *FORM_CODE_NAME : 제형 정보
 *
 *MARK_CODE_FRONT_ANAL : 앞면 분석용 인쇄 정보
 *
 *MARK_CODE_BACK_ANAL : 뒷면 분석용 인쇄 정보
 *
 *MARK_CODE_FRONT_IMG : 앞면 이미지 정보
 *
 *MARK_CODE_BACK_IMG : 뒷면 이미지 정보
 *
 *ITEM_ENG_NAME : 의약품 영문 이름
 *
 *CHANGE_DATE : 정보 변경 일자
 *
 *MARK_CODE_FRONT : 앞면 인쇄 정보
 *
 *MARK_CODE_BACK : 뒷면 인쇄 정보
 *
 *EDI_CODE : 편집 코드
 *
 *BIZRNO : 업체 번호
 *
 * 초기값은 임시 데이터입니다.
 */
@Serializable
data class MedicineVisibilityDto(
    val itemSeq: String = "200906254",
    val itemName: String = "알러샷연질캡슐(세티리진염산염)",
    val entpSeq: String = "19910005",
    val entpName: String = "(주)녹십자",
    val chart: String = "무색 내지 연보라색의 내용물이 든 보라색의 투명한 타원형 연질캡슐",
    val itemImage: String = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154661648918800015",
    val printFront: String = "A-S",
    val printBack: String = "",
    val drugShape: String = "타원형",
    val colorClass1: String = "보라, 투명",
    val colorClass2: String = "",
    val lineFront: String = "",
    val lineBack: String = "",
    val lengLong: String = "12.50",
    val lengShort: String = "7.58",
    val thick: String = "7.58",
    val imgRegistTs: String = "20100305",
    val classNo: String = "01410",
    val className: String = "항히스타민제",
    val etcOtcName: String = "일반의약품",
    val itemPermitDate: String = "20090812",
    val formCodeName: String = "연질캡슐제, 액상",
    val markCodeFrontAnal: String = "",
    val markCodeBackAnal: String = "",
    val markCodeFrontImg: String = "",
    val markCodeBackImg: String = "",
    val itemEngName: String = "Allershot Soft Cap.",
    val changeDate: String = "20230419",
    val markCodeFront: String = "",
    val markCodeBack: String = "",
    val ediCode: String = "",
    val bizrno: String = "3038117108"
)