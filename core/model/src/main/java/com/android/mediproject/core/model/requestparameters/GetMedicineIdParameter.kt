package com.android.mediproject.core.model.requestparameters


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMedicineIdParameter(
    @SerialName("ENTP_NAME") val entpName: String, // (주)한국얀센
    @SerialName("ITEM_INGR_NAME") val itemIngrName: String, // 덱스트로메토르판브롬화수소산염수화물/슈도에페드린염산염/아세트아미노펜/클로르페니라민말레인산염
    @SerialName("ITEM_NAME") val itemName: String, // 타이레놀콜드-에스정(수출명:TylenolColdTablet,TylenolColdCaplet)
    @SerialName("ITEM_SEQ") val itemSeq: String, // 200302348
    @SerialName("PRDUCT_TYPE") val productType: String, // [01140]해열.진통.소염제
    @SerialName("SPCLTY_PBLC") val medicineType: String) {

    fun toMap(): Map<String, String> = mapOf(
        "ENTP_NAME" to entpName,
        "ITEM_INGR_NAME" to itemIngrName,
        "ITEM_NAME" to itemName,
        "ITEM_SEQ" to itemSeq,
        "PRDUCT_TYPE" to productType,
        "SPCLTY_PBLC" to medicineType
    )
}