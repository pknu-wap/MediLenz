package com.android.mediproject.core.model.requestparameters

import com.android.mediproject.core.model.medicine.common.producttype.FilterMedicationProductType
import java.io.Serializable

data class ApprovalListSearchParameter(
    val itemName: String?,
    val entpName: String?,
    val medicationProductType: FilterMedicationProductType,
) : Serializable
