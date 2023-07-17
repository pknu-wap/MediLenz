package com.android.mediproject.core.model.medicine.image

/**
 * 의약품 이미지 캐시 데이터
 *
 * @param itemSequence 의약품 고유코드
 * @param url 이미지 URL
 */
data class MedicineImageCacheEntity(
    val itemSequence: String,
    val url: String,
)
