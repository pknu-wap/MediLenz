package com.android.mediproject.core.ai.classification


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationMetaDataEntity(
    @SerialName("classes") private val classes: List<String>,
) {
    fun getClass(idx: Int) = classes[idx]
}
