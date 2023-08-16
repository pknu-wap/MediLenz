package com.android.mediproject.core.ai.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import java.util.LinkedList

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val results = LinkedList<RectF>()

    private val boundingBoxes = mutableListOf<RectF>()

    private val boxPaint = ObjectBitmapCreator.boxPaint
    private var scaleFactor: Float = 1f
    private val roundCornerRadius = ObjectBitmapCreator.ROUND_CORNER_RADIUS

    var resizedWidth: Int = 0
    var resizeHeight: Int = 0

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (boundingBox in results) {
            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            canvas.drawRoundRect(RectF(left, top, right, bottom), roundCornerRadius, roundCornerRadius, boxPaint)
        }
    }

    fun setResults(
        boundingBoxes: List<RectF>,
        capturedImageWidth: Int,
        capturedImageHeight: Int,
    ) {
        synchronized(results) {
            results.clear()
            results.addAll(boundingBoxes)
        }

        resizedWidth = capturedImageWidth
        resizeHeight = capturedImageHeight

        scaleFactor = maxOf(width.toFloat() / capturedImageHeight, height.toFloat() / capturedImageHeight)
    }

    fun capture() = synchronized(results) {
        boundingBoxes.clear()
        boundingBoxes.addAll(results.toList())
        boundingBoxes.toList()
    }

}
