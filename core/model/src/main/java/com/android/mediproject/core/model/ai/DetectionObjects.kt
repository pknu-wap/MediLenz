package com.android.mediproject.core.model.ai

import android.graphics.Bitmap
import org.tensorflow.lite.task.gms.vision.detector.Detection

/**
 * 검출결과
 *
 * @param detection 검출된 객체
 * @param backgroundImage 전체 배경 이미지
 */
data class DetectionObjects(
    val detection: List<DetectionObject>, val backgroundImage: Bitmap, val width: Int, val height: Int,
)

data class DetectionObject(
    val detection: Detection, val image: Bitmap, var onClick: (() -> Unit)? = null,
)
