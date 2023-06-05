package com.android.mediproject.feature.camera.tflite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.LinkedList
import kotlin.math.max

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var _results: List<Detection> = LinkedList<Detection>()
    val results: List<Detection>
        get() = _results.toMutableList()
    private var boxPaint = Paint()
    private var scaleFactor: Float = 1f

    var imgwidth: Int = 0
    var imgHeight: Int = 0

    init {
        initPaints()
    }

    fun clear() {
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        boxPaint.color = Color.WHITE
        boxPaint.strokeWidth = 6f
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (result in _results) {
            val boundingBox = result.boundingBox

            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)
        }
    }

    fun setResults(
        detectionResults: List<Detection>,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        _results = detectionResults

        imgwidth = imageWidth
        imgHeight = imageHeight

        // PreviewView is in FILL_START mode. So we need to scale up the bounding box to match with
        // the size that the captured images will be displayed.
        scaleFactor = max(imgwidth * 1f / imageWidth, imgHeight * 1f / imageHeight)
    }

}