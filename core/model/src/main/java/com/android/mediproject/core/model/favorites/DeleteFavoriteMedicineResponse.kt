package com.android.mediproject.core.model.favorites


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteFavoriteMedicineResponse(
    @SerialName("message") val message: String
)