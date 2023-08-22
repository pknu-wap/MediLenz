package com.android.mediproject.core.model.ai


import kotlinx.serialization.Serializable

@Serializable
open class BaseVertexAiResponse {
    val error: Error? = null

    @Serializable
    data class Error(
        val code: Int, // 400
        val message: String, // Provided image is not valid.
        val status: String)
}