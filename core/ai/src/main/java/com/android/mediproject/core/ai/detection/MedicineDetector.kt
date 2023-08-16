package com.android.mediproject.core.ai.detection

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.android.mediproject.core.ai.AiModel
import com.android.mediproject.core.ai.model.DetectionResultEntity

interface MedicineDetector : AiModel {
    fun detect(image: ImageProxy, bitmapBuffer: Bitmap): Result<DetectionResultEntity>
}
