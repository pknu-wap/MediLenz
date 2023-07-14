package com.android.mediproject.core.model.favoritemedicine

data class FavoriteMedicineMoreDto(
    val id: Int,
    val itemName: String,
    val entpName: String,
    val itemIngrName: String,
    val spcltyPblc: String
)

fun FavoriteMedicineListResponse.Medicine.toFavoriteMedicineMoreDto() =
    FavoriteMedicineMoreDto(
        id = id,
        itemName = itemName,
        entpName = entpName,
        itemIngrName = itemIngrName,
        spcltyPblc = spcltyPblc
    )