package com.android.mediproject.core.data.medicineid

import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineIdRepositoryImpl @Inject constructor(
    private val medicineIdDataSource: MedicineIdDataSource,
) : MedicineIdRepository {
    override fun getMedicineId(getMedicineIdParameter: GetMedicineIdParameter): Flow<Result<MedicineIdResponse>> =
        medicineIdDataSource.getMedicineId(getMedicineIdParameter)
}
