package com.android.mediproject.core.model.favoritemedicine


data class FavoriteMedicineDto(
    val itemSeq: String,
    val medicineName: String
)

fun FavoriteMedicineListResponse.Medicine.toInterestedMedicineDto() =
    FavoriteMedicineDto(itemSeq = itemSeq, medicineName = itemName)
