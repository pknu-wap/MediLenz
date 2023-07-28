package com.android.mediproject.core.model.favoritemedicine

import com.android.mediproject.core.model.servercommon.ServerQueryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteMedicineListResponse(
    val medicineList: List<Medicine>,
) : ServerQueryResponse() {
    @Serializable
    data class Medicine(
        @SerialName("ENTP_NAME")
        val entpName: String,
        @SerialName("ID")
        val id: Int,
        @SerialName("ITEM_INGR_NAME")
        val itemIngrName: String,
        @SerialName("ITEM_NAME")
        val itemName: String,
        @SerialName("ITEM_SEQ")
        val itemSeq: String,
        @SerialName("PRDUCT_TYPE")
        val prductType: String,
        @SerialName("SPCLTY_PBLC")
        val spcltyPblc: String,
    )
}
