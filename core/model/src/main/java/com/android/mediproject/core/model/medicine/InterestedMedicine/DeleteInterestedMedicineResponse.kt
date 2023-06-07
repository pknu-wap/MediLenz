package com.android.mediproject.core.model.medicine.InterestedMedicine


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteInterestedMedicineResponse(
    @SerialName("message") val message: String
)