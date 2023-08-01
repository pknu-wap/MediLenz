package com.android.mediproject.core.model.medicine.medicinedetailinfo

import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.medicine.common.cancel.MedicineCancelStatus
import com.android.mediproject.core.model.medicine.common.cancel.MedicineCancelStatusMapper
import com.android.mediproject.core.model.medicine.common.producttype.MedicationProductType
import com.android.mediproject.core.model.toLocalDate
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.model.util.parseXmlString

/**
 * 의약품 상세 허가 정보
 *
 * @param atcCode ATC 코드
 * @param barCode 바코드
 * @param businessRegistrationNumber 사업자등록번호
 * @param cancelStatus 취소 상태
 * @param changeDate 변경일자
 * @param chart 성상
 * @param consignmentManufacturer 제조및수입사
 * @param docText ?
 * @param ediCode EDI코드
 * @param eeDocData 의약품 효능효과
 * @param entpEnglishName 제조사의 영문 이름입니다.
 * @param entpName 제조사의 이름입니다.
 * @param entpNumber 제조사의 번호입니다.
 * @param medicationProductType 전문의약품 코드입니다.
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
 * @param nbDocData 사용 상의 주의사항
 * @param nbDocId NB 문서의 식별자(ID)입니다.
 * @param newDrugClassName 새로운 약물 분류 이름입니다.
 * @param packUnit 패키지 단위입니다.
 * @param permitKindName 허가 종류 이름입니다.
 * @param pnDocData PN 문서 데이터입니다.
 * @param reexamTarget 재심사 대상입니다.
 * @param storageMethod 저장 방법입니다.
 * @param totalContent 총 함량입니다.
 * @param udDocData 용법 용량
 * @param validTerm 유효 기간입니다. 제조일로부터의 개월 수를 나타냅니다.
 * @param medicineIdInServer AWS에 저장된 의약품 ID
 * @param existsMedicineIdInServer AWS에 저장된 의약품 ID가 있는지 여부
 */
data class MedicineDetail(
    val atcCode: String,
    val barCode: String,
    val businessRegistrationNumber: String,
    val cancelStatus: MedicineCancelStatus,
    val changeDate: DateTimeValue,
    val chart: String,
    val consignmentManufacturer: String,
    val docText: String,
    val ediCode: String,
    val eeDocData: XMLParsedResult,
    val entpEnglishName: String,
    val entpName: String,
    val entpNumber: String,
    val medicationProductType: MedicationProductType,
    val gbnName: String,
    val industryType: String,
    val ingredientName: String,
    val insertFileUrl: String,
    val itemEnglishName: String,
    val itemName: String,
    val itemPermitDate: DateTimeValue,
    val itemSequence: String,
    val mainIngredientEnglish: String,
    val mainItemIngredient: String,
    val makeMaterialFlag: String,
    val materialName: String,
    val narcoticKindCode: String,
    val nbDocData: XMLParsedResult,
    val nbDocId: String,
    val newDrugClassName: String,
    val packUnit: String,
    val permitKindName: String,
    val pnDocData: XMLParsedResult,
    val reexamTarget: String,
    val storageMethod: String,
    val totalContent: String,
    val udDocData: XMLParsedResult,
    val validTerm: String,
    val medicineIdInServer: Long,
    val existsMedicineIdInServer: Boolean,
)

fun MedicineDetailInfoResponse.Item.toMedicineDetail(medicineIdInServer: Long = 0L) = MedicineDetail(
    atcCode = atcCode,
    barCode = barCode,
    businessRegistrationNumber = businessRegistrationNumber,
    chart = chart,
    consignmentManufacturer = consignmentManufacturer,
    docText = docText,
    ediCode = ediCode,
    eeDocData = eeDocData.parseXmlString(),
    entpEnglishName = entpEnglishName,
    entpName = entpName,
    entpNumber = entpNumber,
    medicationProductType = MedicationProductType.valueOf(etcOtcCode),
    gbnName = gbnName,
    industryType = industryType,
    ingredientName = ingredientName,
    insertFileUrl = insertFileUrl,
    itemEnglishName = itemEnglishName,
    itemName = itemName,
    itemPermitDate = itemPermitDate.toLocalDate("yyyyMMdd"),
    itemSequence = itemSequence,
    mainIngredientEnglish = mainIngredientEnglish,
    mainItemIngredient = mainItemIngredient,
    makeMaterialFlag = makeMaterialFlag,
    materialName = materialName,
    narcoticKindCode = narcoticKindCode,
    nbDocData = nbDocData.parseXmlString(),
    nbDocId = nbDocId,
    newDrugClassName = newDrugClassName,
    packUnit = packUnit,
    permitKindName = permitKindName,
    pnDocData = pnDocData.parseXmlString(),
    reexamTarget = reexamTarget,
    storageMethod = storageMethod,
    totalContent = totalContent,
    udDocData = udDocData.parseXmlString(),
    validTerm = validTerm,
    medicineIdInServer = medicineIdInServer,
    existsMedicineIdInServer = medicineIdInServer != 0L,
    cancelStatus = MedicineCancelStatusMapper.map(cancelName, cancelDate),
    changeDate = changeDate.toLocalDate("yyyyMMdd"),
)
