package com.android.mediproject.core.ai.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import com.android.mediproject.core.ai.model.CapturedDetectionEntity

object ObjectBitmapCreator {

    const val ROUND_CORNER_RADIUS = 10f
    
    val boxPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 12f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.SQUARE
        isAntiAlias = true
    }

    fun createBitmapWithObjects(capturedDetectionEntity: CapturedDetectionEntity): Bitmap {
        val outBitmap = capturedDetectionEntity.capturedImage.applyCanvas {
            for (item in capturedDetectionEntity.items) {
                val rect = capturedDetectionEntity.scale(item.boundingBox)
                drawRoundRect(rect, ROUND_CORNER_RADIUS, ROUND_CORNER_RADIUS, boxPaint)
            }
        }

        return outBitmap
    }
}
