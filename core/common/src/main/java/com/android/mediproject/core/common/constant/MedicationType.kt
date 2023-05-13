package com.android.mediproject.core.common.constant

/**
 * 약품 타입
 *
 * @property SPECIALTY 전문의약품
 * @property GENERAL 일반의약품
 * @property ALL 전체
 */
enum class MedicationType(val description: String) {
    ALL(""),
    SPECIALTY("전문"),
    GENERAL("일반"),
}