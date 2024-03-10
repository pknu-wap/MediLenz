package com.android.mediproject.core.ai.util

import android.graphics.Bitmap
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.scale
import com.android.mediproject.core.ai.model.CapturedDetectionEntity
import com.android.mediproject.core.ai.util.SharedObjectDetectionViewProperty.draw
import java.lang.ref.WeakReference

object ObjectBitmapCreator {
    private const val TARGET_SIZE = 224

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

    fun resizeBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.let { src ->
            if (src.width == TARGET_SIZE && src.height == TARGET_SIZE) {
                src
            } else {
                val resizeRatio: Float = maxOf(bitmap.width, bitmap.height).let { max ->
                    if (max > TARGET_SIZE) TARGET_SIZE / max.toFloat() else max.toFloat() / TARGET_SIZE
                }
                val resizedBitmap = WeakReference(
                    bitmap.scale(
                        (bitmap.width * resizeRatio).toInt(),
                        (bitmap.height * resizeRatio).toInt(),
                    ),
                ).get()!!
                resizedBitmap
            }
        }
    }
}
