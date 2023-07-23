package com.android.mediproject.core.model.remote.granule

import com.android.mediproject.core.toLocalDate
import java.time.LocalDate


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
data class GranuleIdentificationInfoDto(
    val bizrNo: String, // 3038117108
    val changeDate: LocalDate, // 20230419
    val chart: String, // 무색 내지 연보라색의 내용물이 든 보라색의 투명한 타원형 연질캡슐
    val className: String, // 항히스타민제
    val classNo: String, // 01410
    val colorClass1: String, // 보라, 투명
    val colorClass2: String?, // null
    val drugShape: String, // 타원형
    val ediCode: String?, // null
    val entpName: String, // (주)녹십자
    val entpSeq: String, // 19910005
    val etcOtcName: String, // 일반의약품
    val formCodeName: String, // 연질캡슐제, 액상
    val imgRegistTs: LocalDate, // 20100305
    val itemEngName: String, // Allershot Soft Cap.
    val itemImage: String, // https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154661648918800015
    val itemName: String, // 알러샷연질캡슐(세티리진염산염)
    val itemPermitDate: LocalDate, // 20090812
    val itemSeq: String, // 200906254
    val lengLong: String, // 12.50
    val lengShort: String, // 7.58
    val lineBack: String?, // null
    val lineFront: String?, // null
    val markCodeBack: String?, // null
    val markCodeBackAnal: String, val markCodeBackImg: String, val markCodeFront: String?, // null
    val markCodeFrontAnal: String, val markCodeFrontImg: String, val printBack: String?, // null
    val printFront: String?, // A-S
    val thick: String?, // 7.58
)

fun GranuleIdentificationInfoResponse.Body.Item.toDto() = GranuleIdentificationInfoDto(
    bizrNo = bizrNo,
    changeDate = changeDate.toLocalDate("yyyyMMdd"),
    chart = chart,
    className = className,
    classNo = classNo,
    colorClass1 = colorClass1,
    colorClass2 = colorClass2,
    drugShape = drugShape,
    ediCode = ediCode,
    entpName = entpName,
    entpSeq = entpSeq,
    etcOtcName = etcOtcName,
    formCodeName = formCodeName,
    imgRegistTs = imgRegistTs.toLocalDate("yyyyMMdd"),
    itemEngName = itemEngName,
    itemImage = itemImage,
    itemName = itemName,
    itemPermitDate = itemPermitDate.toLocalDate("yyyyMMdd"),
    itemSeq = itemSeq,
    lengLong = lengLong,
    lengShort = lengShort,
    lineBack = lineBack,
    lineFront = lineFront,
    markCodeBack = markCodeBack,
    markCodeBackAnal = markCodeBackAnal,
    markCodeBackImg = markCodeBackImg,
    markCodeFront = markCodeFront,
    markCodeFrontAnal = markCodeFrontAnal,
    markCodeFrontImg = markCodeFrontImg,
    printBack = printBack,
    printFront = printFront,
    thick = thick,
)
