package com.android.mediproject.core.ai.util

import android.graphics.Bitmap
import androidx.core.graphics.applyCanvas
import com.android.mediproject.core.ai.model.CapturedDetectionEntity
import com.android.mediproject.core.ai.util.SharedObjectDetectionViewProperty.draw

object ObjectBitmapCreator {

    fun createBitmapWithObjects(capturedDetectionEntity: CapturedDetectionEntity): Bitmap {
        val outBitmap = capturedDetectionEntity.capturedImage.applyCanvas {
            draw(
                capturedDetectionEntity.items.map {
                    SharedObjectDetectionViewProperty.Object(
                        it.label,
                        it.confidence,
                        it.boundingBox,
                    )
                },
                capturedDetectionEntity.widthScaleFactor,
                capturedDetectionEntity.heightScaleFactor,
            )
        }
        return outBitmap
    }
}
