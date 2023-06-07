package com.android.mediproject.core.model.favorites


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFavoriteMedicineResponse(
    @SerialName("favoriteMedicineID") val favoriteMedicineID: Int, // 86
    @SerialName("message") val message: String
)