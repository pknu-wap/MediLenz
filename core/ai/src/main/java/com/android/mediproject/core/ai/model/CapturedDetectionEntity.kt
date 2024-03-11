package com.android.mediproject.core.ai.model

import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Size

/**
 * 약 인식 화면에서 버튼을 눌렀을때 사진 정보가 담기는 클래스
 *
 * @property capturedImage 캡처된 사진
 * @property originalImageSize 사진 실제 크기
 * @property resizedImageSize 조정된 사진 크기
 * @property items 검출된 약 객체 목록
 */
data class CapturedDetectionEntity(
    val items: List<Item>, val capturedImage: Bitmap, val originalImageSize: Size,
    val resizedImageSize: Size,
) {
    val widthScaleFactor = originalImageSize.width.toFloat() / resizedImageSize.width
    val heightScaleFactor = originalImageSize.height.toFloat() / resizedImageSize.height

    fun separateImages() {
        items.forEach { item ->
            val scale = scale(item.boundingBox)
            item.bitmap = Bitmap.createBitmap(capturedImage, scale.left.toInt(), scale.top.toInt(), scale.width().toInt(), scale.height().toInt())
        }
    }

    private fun scale(rect: RectF): RectF = rect.run {
        val top = top * heightScaleFactor
        val bottom = bottom * heightScaleFactor
        val left = left * widthScaleFactor
        val right = right * widthScaleFactor

        val width = right - left
        val height = bottom - top
        RectF(left, top, right, bottom)
    }

    data class Item(
        val boundingBox: RectF,
        val label: String,
        val confidence: Int,
        var bitmap: Bitmap? = null,
    )
}
