package com.android.mediproject.feature.camera

import android.util.Size
import android.view.View
import androidx.camera.view.PreviewView
import androidx.databinding.BindingAdapter
import com.android.mediproject.feature.camera.tflite.camera.OverlayView

object CamBindingAdapter {
    @BindingAdapter(value = ["onCapture", "srcPreview", "overlayView"], requireAll = true)
    @JvmStatic
    fun setOnClick(view: View, onCapture: (DetectedObjectResult) -> Unit, srcPreview: PreviewView, overlayView: OverlayView) {
        view.setOnClickListener {
            onCapture.invoke(
                DetectedObjectResult(
                    detections = overlayView.capture(),
                    backgroundImage = srcPreview.bitmap!!,
                    realWindowSize = Size(overlayView.width, overlayView.height),
                    resizedWindowSize = Size(overlayView.resizedWidth, overlayView.resizeHeight),
                ),
            )
        }
    }
}
