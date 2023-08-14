package com.android.mediproject.core.model.ai

import android.graphics.Bitmap
import org.tensorflow.lite.task.gms.vision.detector.Detection

/**
 * 검출결과
 *
 * @param detection 검출된 객체
 * @param backgroundImage 전체 배경 이미지
 */
data class DetectionResultEntity(
    val detection: List<Object>, val backgroundImage: Bitmap, val width: Int, val height: Int,
) {
    data class Object(
        val detection: Detection, val image: Bitmap, var onClick: (() -> Unit)? = null,
    )
}
