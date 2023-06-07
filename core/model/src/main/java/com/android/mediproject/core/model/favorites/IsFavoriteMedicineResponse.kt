package com.android.mediproject.core.model.favorites


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsFavoriteMedicineResponse(
    @SerialName("isFavorite") val isFavorite: Boolean, // false
    @SerialName("message") val message: String
)