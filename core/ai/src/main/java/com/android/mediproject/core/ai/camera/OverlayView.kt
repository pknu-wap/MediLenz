package com.android.mediproject.core.ai.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import com.android.mediproject.core.ai.util.SharedObjectDetectionViewProperty
import com.android.mediproject.core.ai.util.SharedObjectDetectionViewProperty.draw
import java.util.LinkedList
import javax.inject.Inject

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    @Inject lateinit var objectBitmapCreator: ObjectBitmapCreator

    private val detectionResult = LinkedList<CameraHelper.OnDetectionListener.Object>()

    private var widthScaleFactor: Float = 1f
    private var heightScaleFactor: Float = 1f

    var resizedWidth: Int = 0
    var resizeHeight: Int = 0

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.draw(
            detectionResult.map {
                SharedObjectDetectionViewProperty.Object(
                    it.label,
                    it.confidence,
                    it.boundingBox,
                )
            },
            widthScaleFactor,
            heightScaleFactor,
        )
    }

    fun setResults(
        boundingBoxes: List<CameraHelper.OnDetectionListener.Object>,
        capturedImageWidth: Int,
        capturedImageHeight: Int,
    ) {
        synchronized(detectionResult) {
            detectionResult.clear()
            detectionResult.addAll(boundingBoxes)
        }

        resizedWidth = capturedImageWidth
        resizeHeight = capturedImageHeight

        widthScaleFactor = width.toFloat() / capturedImageWidth
        heightScaleFactor = height.toFloat() / capturedImageHeight
    }

    fun capture() = synchronized(detectionResult) {
        detectionResult.toList()
    }

}
