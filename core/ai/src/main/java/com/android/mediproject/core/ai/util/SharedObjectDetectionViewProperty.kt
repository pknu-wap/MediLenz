package com.android.mediproject.core.ai.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import com.android.mediproject.core.common.util.dpToPx

object SharedObjectDetectionViewProperty {
    private const val ROUND_CORNER_RADIUS = 10f
    private val SPACE_BETWEEN_BOX_AND_TEXT = 4.dpToPx().toFloat()
    private val SPACE_BETWEEN_TEXT = 8.dpToPx().toFloat()

    private val boxPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 4.dpToPx().toFloat()
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.SQUARE
        isAntiAlias = true
    }

    private val labelTextPaint = TextPaint().apply {
        color = Color.parseColor("#22A7CC")
        textSize = 14.dpToPx().toFloat()
        isAntiAlias = true
    }

    private val confidenceTextPaint = TextPaint().apply {
        color = Color.parseColor("#22A7CC")
        textSize = 12.dpToPx().toFloat()
        isAntiAlias = true
    }


    fun Canvas.draw(objects: List<Object>, scaleFactor: Float) {
        for (obj in objects) {
            val boundingBox = obj.boundingBox
            val rect = RectF(
                boundingBox.left * scaleFactor,
                boundingBox.top * scaleFactor,
                boundingBox.right * scaleFactor,
                boundingBox.bottom * scaleFactor,
            )
            drawRoundRect(rect, ROUND_CORNER_RADIUS, ROUND_CORNER_RADIUS, boxPaint)

            val textX = rect.left + SPACE_BETWEEN_BOX_AND_TEXT
            val textY =
                rect.top + labelTextPaint.textSize + SPACE_BETWEEN_BOX_AND_TEXT

            drawText(
                obj.label,
                textX,
                textY,
                labelTextPaint,
            )
            drawText(
                "${obj.confidence}%",
                textX + confidenceTextPaint.measureText(obj.label) + SPACE_BETWEEN_TEXT,
                textY,
                confidenceTextPaint,
            )
        }
    }

    data class Object(
        val label: String,
        val confidence: Int,
        val boundingBox: RectF,
    )
}
