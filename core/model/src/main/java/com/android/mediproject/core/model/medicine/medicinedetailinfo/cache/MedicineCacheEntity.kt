package com.android.mediproject.core.model.medicine.medicinedetailinfo.cache

/**
 * 의약품 상세정보 캐시 데이터
 *
 * @param itemSequence 의약품 고유코드
 * @param json 의약품 상세정보 JSON 응답 문자열
 * @param imageUrl 의약품 이미지 URL
 * @param changeDate 의약품 정보 변경일자
 */
data class MedicineCacheEntity(
    val itemSequence: String,
    val json: String = "",
    val imageUrl: String = "",
    val changeDate: String = "",
)
