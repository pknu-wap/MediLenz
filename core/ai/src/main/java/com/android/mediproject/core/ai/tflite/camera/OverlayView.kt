package com.android.mediproject.core.ai.tflite.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.LinkedList

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val _results = LinkedList<Detection>()

    private val capturedObjects = mutableListOf<Detection>()

    val results: List<Detection>
        get() = _results
    private val boxPaint = ObjectBitmapCreator.boxPaint
    private var scaleFactor: Float = 1f
    private val roundCornerRadius = ObjectBitmapCreator.ROUND_CORNER_RADIUS

    var resizedWidth: Int = 0
    var resizeHeight: Int = 0


    fun clear() {
        _results.clear()
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (result in _results) {
            val boundingBox = result.boundingBox

            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            canvas.drawRoundRect(RectF(left, top, right, bottom), roundCornerRadius, roundCornerRadius, boxPaint)
        }
    }

    fun setResults(
        detectionResults: List<Detection>,
        imageWidth: Int,
        imageHeight: Int,
    ) {
        synchronized(_results) {
            _results.clear()
            _results.addAll(detectionResults)
        }
        resizedWidth = imageWidth
        resizeHeight = imageHeight

        scaleFactor = maxOf(width.toFloat() / imageHeight, height.toFloat() / imageHeight)
    }

    fun capture() = synchronized(_results) {
        capturedObjects.clear()
        capturedObjects.addAll(_results.toList())
        capturedObjects.toList()
    }

}
