package com.android.mediproject.core.model.ai

/**
 * 약 분류 결과
 *
 * @property medicineSeq 의약품 품목 기준 코드
 * @property confidence 정확도
 */
data class ClassificationRecognition(val medicineSeq: String, val confidence: Float) {

    override fun toString(): String {
        return probabilityString
    }

    private val probabilityString = String.format("%.1f%%", confidence * 100.0f)
}