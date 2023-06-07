package com.android.mediproject.core.data.remote.interestedmedicine


import com.android.mediproject.core.model.medicine.InterestedMedicine.AddInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.medicine.InterestedMedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineRepository {
    suspend fun getInterestedMedicineList(): Flow<Result<List<InterestedMedicineListResponse.Medicine>>>

    fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<AddInterestedMedicineResponse>>

    fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>>

    fun isInterestedMedicine(itemSeq: Long): Flow<Result<IsInterestedMedicineResponse>>
}