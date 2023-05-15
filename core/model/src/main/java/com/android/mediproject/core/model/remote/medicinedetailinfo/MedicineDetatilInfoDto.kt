package com.android.mediproject.core.model.remote.medicinedetailinfo

import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.model.util.parseXmlString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.DateTimeFormatter

/**
 * 의약품 상세 허가 정보
 *
 * @param atcCode ATC 코드
 * @param barCode 바코드
 * @param businessRegistrationNumber 사업자등록번호
 * @param cancelDate 취소일자
 * @param cancelName 취소명
 * @param changeDate 변경일자
 * @param chart 성상
 * @param consignmentManufacturer 제조및수입사
 * @param docText 효능효과
 * @param ediCode EDI코드
 * @param eeDocData 의약품 효능효과
 * @param eeDocId 의약품 효능효과 문서의 식별자(ID)입니다.
 * @param entpEnglishName 제조사의 영문 이름입니다.
 * @param entpName 제조사의 이름입니다.
 * @param entpNumber 제조사의 번호입니다.
 * @param etcOtcCode 전문의약품 코드입니다.
 * @param gbnName GBN(General Batch Number)의 이름입니다.
 * @param industryType 산업 유형입니다. 이 경우에는 '의약품'이라고 명시되어 있습니다.
 * @param ingredientName 성분 이름입니다.
 * @param insertFileUrl 삽입 파일의 URL입니다.
 * @param itemEnglishName 제품의 영문 이름입니다.
 * @param itemName 제품 이름입니다.
 * @param itemPermitDate 제품 허가 날짜입니다.
 * @param itemSequence 제품 시퀀스 번호입니다.
 * @param mainIngredientEnglish 주성분의 영문 이름입니다.
 * @param mainItemIngredient 주성분입니다.
 * @param makeMaterialFlag 제조재료 플래그입니다. 완제의약품을 나타냅니다.
 * @param materialName 재료 이름입니다.
 * @param narcoticKindCode 마약 종류 코드입니다.
 * @param nbDocData NB 문서 데이터입니다.
 * @param nbDocId NB 문서의 식별자(ID)입니다.
 * @param newDrugClassName 새로운 약물 분류 이름입니다.
 * @param packUnit 패키지 단위입니다.
 * @param permitKindName 허가 종류 이름입니다.
 * @param pnDocData PN 문서 데이터입니다.
 * @param reexamDate 재심사 날짜입니다.
 * @param reexamTarget 재심사 대상입니다.
 * @param storageMethod 저장 방법입니다.
 * @param totalContent 총 함량입니다.
 * @param udDocData UD 문서 데이터입니다.
 * @param uDDOCID UD 문서의 식별자(ID)입니다.
 * @param validTerm 유효 기간입니다. 제조일로부터의 개월 수를 나타냅니다.
 * @param imgUrl 이미지 URL
 */
data class MedicineDetatilInfoDto(
    val atcCode: String?,
    val barCode: String?,
    val businessRegistrationNumber: String?,
    val cancelDate: LocalDate?,
    val cancelName: String?,
    val changeDate: LocalDate?,
    val chart: String?,
    val consignmentManufacturer: String?,
    val docText: String?,
    val ediCode: String?,
    val eeDocData: XMLParsedResult?,
    val eeDocId: String?,
    val entpEnglishName: String?,
    val entpName: String?,
    val entpNumber: String?,
    val etcOtcCode: String?,
    val gbnName: String?,
    val industryType: String?,
    val ingredientName: String?,
    val insertFileUrl: String?,
    val itemEnglishName: String?,
    val itemName: String?,
    val itemPermitDate: LocalDate?,
    val itemSequence: String?,
    val mainIngredientEnglish: String?,
    val mainItemIngredient: String?,
    val makeMaterialFlag: String?,
    val materialName: String?,
    val narcoticKindCode: String?,
    val nbDocData: XMLParsedResult?,
    val nbDocId: String?,
    val newDrugClassName: String?,
    val packUnit: String?,
    val permitKindName: String?,
    val pnDocData: XMLParsedResult?,
    val reexamDate: LocalDate?,
    val reexamTarget: String?,
    val storageMethod: String?,
    val totalContent: String?,
    val udDocData: XMLParsedResult?,
    val uDDOCID: String?,
    val validTerm: String?,
)

fun MedicineDetailInfoResponse.Body.Item.toDto() = MedicineDetatilInfoDto(
    atcCode = atcCode,
    barCode = barCode,
    businessRegistrationNumber = businessRegistrationNumber,
    cancelDate = cancelDate.toLocalDate(),
    cancelName = cancelName,
    changeDate = changeDate.toLocalDate(),
    chart = chart,
    consignmentManufacturer = consignmentManufacturer,
    docText = docText,
    ediCode = ediCode,
    eeDocData = eeDocData?.parseXmlString(),
    eeDocId = eeDocId,
    entpEnglishName = entpEnglishName,
    entpName = entpName,
    entpNumber = entpNumber,
    etcOtcCode = etcOtcCode,
    gbnName = gbnName,
    industryType = industryType,
    ingredientName = ingredientName,
    insertFileUrl = insertFileUrl,
    itemEnglishName = itemEnglishName,
    itemName = itemName,
    itemPermitDate = itemPermitDate.toLocalDate(),
    itemSequence = itemSequence,
    mainIngredientEnglish = mainIngredientEnglish,
    mainItemIngredient = mainItemIngredient,
    makeMaterialFlag = makeMaterialFlag,
    materialName = materialName,
    narcoticKindCode = narcoticKindCode,
    nbDocData = nbDocData?.parseXmlString(),
    nbDocId = nbDocId,
    newDrugClassName = newDrugClassName,
    packUnit = packUnit,
    permitKindName = permitKindName,
    pnDocData = pnDocData?.parseXmlString(),
    reexamDate = reexamDate.toLocalDate(),
    reexamTarget = reexamTarget,
    storageMethod = storageMethod,
    totalContent = totalContent,
    udDocData = udDocData?.parseXmlString(),
    uDDOCID = uDDOCID,
    validTerm = validTerm,
)

private fun String?.toLocalDate(): LocalDate? = this?.let {
    java.time.LocalDate.parse(it, dateTimeFormatter).toKotlinLocalDate()
}

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")