package com.android.mediproject.core.data.remote.medicineid

import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.flow.Flow

interface MedicineIdRepository {
    fun getMedicineId(getMedicineIdParameter: GetMedicineIdParameter): Flow<Result<MedicineIdResponse>>
}