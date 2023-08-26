package com.android.mediproject.core.ai.detection

import android.graphics.Bitmap
import com.android.mediproject.core.ai.AiModel
import com.android.mediproject.core.ai.model.DetectionResultEntity

interface MedicineDetector : AiModel {
    fun detect(bitmap: Bitmap): Result<DetectionResultEntity>

}
