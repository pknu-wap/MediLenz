package com.android.mediproject.feature.camera.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.applyCanvas
import com.android.mediproject.core.model.ai.DetectionResultEntity

object ObjectBitmapCreator {

    const val ROUND_CORNER_RADIUS = 10f
    val boxPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 12f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.SQUARE
        isAntiAlias = true
    }

    fun createBitmapWithObjects(objects: DetectionResultEntity): Bitmap {
        val srcImageWidth = objects.width
        val srcImageHeight = objects.height
        val scaleFactor = maxOf(objects.backgroundImage.width.toFloat() / srcImageHeight, objects.backgroundImage.height.toFloat() / srcImageHeight)

        val outBitmap = objects.backgroundImage.applyCanvas {
            objects.detection.map { it.detection }.forEach { detection ->
                val boundingBox = detection.boundingBox

                val top = boundingBox.top * scaleFactor
                val bottom = boundingBox.bottom * scaleFactor
                val left = boundingBox.left * scaleFactor
                val right = boundingBox.right * scaleFactor

                drawRoundRect(RectF(left, top, right, bottom), ROUND_CORNER_RADIUS, ROUND_CORNER_RADIUS, boxPaint)
            }
        }

        return outBitmap
    }
}
