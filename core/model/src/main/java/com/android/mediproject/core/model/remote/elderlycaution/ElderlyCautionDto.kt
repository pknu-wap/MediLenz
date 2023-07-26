package com.android.mediproject.core.model.remote.elderlycaution

import com.android.mediproject.core.toLocalDate
import java.time.LocalDate

/**
 * 노인안전정보
 *
 * @param changeDate 변경일자
 * @param chart 모양
 * @param classCode 분류코드
 * @param className 분류명
 * @param entpName 업체명
 * @param etcOtcName 구분
 * @param formName 제형
 * @param ingrCode 성분코드
 * @param ingrEngName 성분영문명
 * @param ingrEngNameFull 성분영문명(전체)
 * @param ingrName 성분명
 * @param itemName 품목명
 * @param itemSeq 품목일련번호
 * @param itemPermitDate 허가일자
 * @param mainIngr 성분내용
 * @param minIngr 성분내용
 * @param mixType 성분유형
 * @param notificationDate 통지일자
 * @param prohibitionContent 금기내용
 * @param remark 비고
 * @param typeName 유형
 */
data class ElderlyCautionDto(
    val changeDate: LocalDate?, // 20221201
    val chart: String, // 청색의 원형 필름코팅정
    val classCode: String, // 01170
    val className: String, // 정신신경용제
    val entpName: String, // 환인제약(주)
    val etcOtcName: String, // 전문의약품
    val formName: String, // 필름코팅정
    val ingrCode: String, // D000809
    val ingrEngName: String, // Amitriptyline
    val ingrEngNameFull: String, // Amitriptyline(아미트리프틸린)
    val ingrName: String, // 아미트리프틸린
    val itemName: String, // 에나폰정10밀리그램(아미트리프틸린염산염)
    val itemPermitDate: LocalDate?, // 19700220
    val itemSeq: String, // 197000079
    val mainIngr: String, // [M223101]아미트리프틸린염산염
    val minIngr: String, // [M223101]아미트리프틸린염산염
    val mixType: String, // 단일
    val notificationDate: LocalDate?, // 20150728
    val prohibitionContent: String, // 노인에서의 삼환계 항우울제 사용은 기립성 저혈압, 비틀거림, 항콜린작용에 의한 구갈, 배뇨곤란, 변비, 안내압항진 등이 나타나기 쉬움으로 소량으로 신중투여
    val remark: String?, // null
    val typeName: String, // 노인주의
)

fun ElderlyCautionResponse.Body.Item.toElderlyCautionDto() = ElderlyCautionDto(
    changeDate = changeDate.toLocalDate("yyyyMMdd"),
    chart = cHART,
    classCode = cLASSCODE,
    className = cLASSNAME,
    entpName = entpName,
    etcOtcName = etcOtcName,
    formName = fORMNAME,
    ingrCode = iNGRCODE,
    ingrEngName = iNGRENGNAME,
    ingrEngNameFull = iNGRENGNAMEFULL,
    ingrName = iNGRNAME,
    itemName = iTEMNAME,
    itemPermitDate = iTEMPERMITDATE.toLocalDate("yyyyMMdd"),
    itemSeq = iTEMSEQ,
    mainIngr = mAININGR,
    minIngr = mIXINGR,
    mixType = mIXTYPE,
    notificationDate = nOTIFICATIONDATE.toLocalDate("yyyyMMdd"),
    prohibitionContent = pROHBTCONTENT,
    remark = rEMARK,
    typeName = tYPENAME,
)
