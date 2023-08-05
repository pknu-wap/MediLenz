package com.android.mediproject.core.network.datasource.medicineid

import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.flow.Flow

interface MedicineIdDataSource {
    fun getMedicineId(getMedicineIdParameter: GetMedicineIdParameter): Flow<Result<MedicineIdResponse>>
}