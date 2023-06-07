package com.android.mediproject.feature.camera.tflite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.LinkedList
import kotlin.math.max

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var _results: List<Detection> = LinkedList<Detection>()
    val results: List<Detection>
        get() = _results.toMutableList()
    private val boxPaint = Paint()
    private var scaleFactor: Float = 1f
    private val roundCornerRadius = 8f

    var resizedWidth: Int = 0
    var resizeHeight: Int = 0

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
        boxPaint.strokeWidth = 12f
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeCap = Paint.Cap.SQUARE
        boxPaint.isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (result in _results) {
            val boundingBox = result.boundingBox

            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            Log.d("Overlay",
                "draw rect : left : $left, top : $top, right : $right, bottom : $bottom, width : $width, height : $height")

            canvas.drawRoundRect(RectF(left, top, right, bottom), roundCornerRadius, roundCornerRadius, boxPaint)
        }
    }

    fun setResults(
        detectionResults: List<Detection>,
        imageWidth: Int,
        imageHeight: Int,
    ) {
        _results = detectionResults

        resizedWidth = imageWidth
        resizeHeight = imageHeight

        scaleFactor = max(width * 1f / imageHeight, height * 1f / imageHeight)

        Log.d("Overlay",
            "setResults: scaleFactor : $scaleFactor, resizedWidth : $resizedWidth, resizeHeight : $resizeHeight, width : $width, height : $height")
    }

}