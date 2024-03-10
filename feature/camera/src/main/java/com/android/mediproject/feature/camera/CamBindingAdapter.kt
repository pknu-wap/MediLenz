package com.android.mediproject.feature.camera

import android.util.Size
import android.view.View
import androidx.camera.view.PreviewView
import androidx.databinding.BindingAdapter
import com.android.mediproject.core.ai.camera.OverlayView
import com.android.mediproject.core.ai.model.CapturedDetectionEntity

object CamBindingAdapter {
    @BindingAdapter(value = ["onCapture", "srcPreview", "overlayView"], requireAll = true)
    @JvmStatic
    fun setOnClick(view: View, onCapture: (CapturedDetectionEntity) -> Unit, srcPreview: PreviewView, overlayView: OverlayView) {
        view.setOnClickListener {
            if (srcPreview.previewStreamState.isInitialized) {
                onCapture.invoke(
                    CapturedDetectionEntity(
                        items = overlayView.capture().map {
                            CapturedDetectionEntity.Item(it.boundingBox, it.label, it.confidence)
                        },
                        capturedImage = srcPreview.bitmap!!,
                        originalImageSize = Size(overlayView.width, overlayView.height),
                        resizedImageSize = Size(overlayView.resizedWidth, overlayView.resizeHeight),
                    ),
                )
            }
        }
    }
}
