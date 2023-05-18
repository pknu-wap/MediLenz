package com.android.mediproject.core.model.parameters

import com.android.mediproject.core.model.constants.MedicationType
import java.io.Serializable

data class ApprovalListSearchParameter(
    val itemName: String?,
    val entpName: String?,
    val medicationType: MedicationType,
) : Serializable