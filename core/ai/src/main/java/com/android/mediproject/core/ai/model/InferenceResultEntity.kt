package com.android.mediproject.core.ai.model

import android.graphics.RectF
import android.util.Size


abstract class InferenceResultEntity<T : InferenceItem> {
    abstract val items: List<T>
}

data class DetectionResultEntity(
    val inferencedImageSize: Size, override val items: List<Item>,
) : InferenceResultEntity<DetectionResultEntity.Item>() {
    data class Item(val boundingBox: RectF, val label: String, val confidence: Int) : InferenceItem
}

data class ClassificationResultEntity(
    override val items: List<Item>,
) : InferenceResultEntity<ClassificationResultEntity.Item>() {
    data class Item(val itemSeq: String, private val scoreF: Float) : InferenceItem {
        val score: Int = (scoreF * 100f).toInt()
    }
}
