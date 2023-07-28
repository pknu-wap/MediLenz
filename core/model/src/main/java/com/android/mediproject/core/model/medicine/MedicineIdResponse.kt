package com.android.mediproject.core.model.medicine

import com.android.mediproject.core.model.servercommon.ServerQueryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineIdResponse(
    @SerialName("medicineId") val medicineId: Long,
) : ServerQueryResponse()
