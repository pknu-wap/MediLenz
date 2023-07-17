package com.android.mediproject.core.model.medicine.medicinedetailinfo.cache

/**
 * 의약품 상세정보 캐시 데이터
 *
 * @param itemSequence 의약품 고유코드
 * @param json 의약품 상세정보 JSON 응답 문자열
 */
data class MedicineDetailCacheEntity(
    val itemSequence: String,
    val json: String,
)
