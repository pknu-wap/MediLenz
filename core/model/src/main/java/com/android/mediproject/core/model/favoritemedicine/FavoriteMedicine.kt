package com.android.mediproject.core.model.favoritemedicine


data class FavoriteMedicine(
    val itemSeq: String,
    val medicineName: String
)

fun FavoriteMedicineListResponse.Medicine.toFavoriteMedicine() =
    FavoriteMedicine(itemSeq = itemSeq, medicineName = itemName)
