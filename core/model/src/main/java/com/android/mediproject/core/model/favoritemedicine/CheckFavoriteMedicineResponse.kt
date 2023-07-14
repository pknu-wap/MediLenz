package com.android.mediproject.core.model.favoritemedicine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckFavoriteMedicineResponse(
    @SerialName("isFavorite") val isFavorite: Boolean, // false
    @SerialName("message") val message: String
)