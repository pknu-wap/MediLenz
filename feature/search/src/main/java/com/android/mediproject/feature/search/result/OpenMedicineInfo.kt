package com.android.mediproject.feature.search.result

import com.android.mediproject.core.model.navargs.MedicineInfoArgs

sealed class EventState {
    data class OpenMedicineInfo(val medicineInfoArgs: MedicineInfoArgs) : EventState()
}
