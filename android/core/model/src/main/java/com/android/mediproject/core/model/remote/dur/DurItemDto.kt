package com.android.mediproject.core.model.remote.dur


/**
 * @param barCode 바코드
 * @param bizRno 사업자등록번호
 * @param cancelDate 취소일
 * @param cancelName 취소명
 * @param changeDate 변경일
 * @param chart 차트
 * @param classNo 클래스 번호
 * @param ediCode EDI 코드
 * @param eeDocId EE 문서 ID
 * @param entpName 기업 이름
 * @param etcOtcCode 기타 OTC 코드
 * @param insertFile 삽입 파일
 * @param itemName 아이템 이름
 * @param itemPermitDate 아이템 허가 날짜
 * @param itemSeq 아이템 순서
 * @param materialName 재료 이름
 * @param nbDocId NB 문서 ID
 * @param packUnit 패키지 단위
 * @param reexamDate 재검사 날짜
 * @param reexamTarget 재검사 대상
 * @param storageMethod 저장 방법
 * @param typeCode 유형 코드
 * @param typeName 유형 이름
 * @param udDocId UD 문서 ID
 * @param validTerm 유효 기간
 */
data class DurItemDto(
    val barCode: String, // 8806421025729
    val bizRno: String, // 1188100601
    val cancelDate: String?, // null
    val cancelName: String?, // 정상
    val changeDate: String?, // 2019August26th
    val chart: String, // 미황색의 원형 정제
    val classNo: String, // [141]항히스타민제
    val ediCode: String?, // 642102570
    val eeDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/EE
    val entpName: String, // (주)유한양행
    val etcOtcCode: String, // 일반의약품
    val insertFile: String?, // HTTP://WWW.HEALTH.KR/IMAGES/INSERT_PDF/In_A11A0450A0085_00.pdf
    val itemName: String, // 페니라민정(클로르페니라민말레산염)
    val itemPermitDate: String, // 1960October10th
    val itemSeq: String, // 196000011
    val materialName: String, // 클로르페니라민말레산염,,2.0,밀리그램,KP,
    val nbDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/NB
    val packUnit: String?, // 1000정/병
    val reexamDate: String?, // null
    val reexamTarget: String?, // null
    val storageMethod: String?, // 실온, 건소, 밀폐용기,
    val typeCode: String?, // D,F,I
    val typeName: String?, // 용량주의,노인주의,첨가제주의
    val udDocId: String?, // HTTPS://NEDRUG.MFDS.GO.KR/PBP/CMN/PDFVIEWER/196000011/UD
    val validTerm: String? // 제조일로부터 36 개월
)

fun DurResponse.Body.Item.toDto() = DurItemDto(
    barCode = barCode ?: "",
    bizRno = bizRno ?: "",
    cancelDate = cancelDate,
    cancelName = cancelName,
    changeDate = changeDate,
    chart = chart ?: "",
    classNo = classNo ?: "",
    ediCode = ediCode,
    eeDocId = eeDocId,
    entpName = entpName ?: "",
    etcOtcCode = etcOtcCode ?: "",
    insertFile = insertFile,
    itemName = itemName ?: "",
    itemPermitDate = itemPermitDate ?: "",
    itemSeq = itemSeq ?: "",
    materialName = materialName ?: "",
    nbDocId = nbDocId,
    packUnit = packUnit,
    reexamDate = reexamDate,
    reexamTarget = reexamTarget,
    storageMethod = storageMethod,
    typeCode = typeCode,
    typeName = typeName,
    udDocId = udDocId,
    validTerm = validTerm
)