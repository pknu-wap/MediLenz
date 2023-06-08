package com.android.mediproject.core.data.remote.interestedmedicine


import com.android.mediproject.core.model.interestedmedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.interestedmedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.interestedmedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.interestedmedicine.NewInterestedMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineRepository {
    suspend fun getInterestedMedicineList(): Flow<Result<List<InterestedMedicineListResponse.Medicine>>>

    fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<NewInterestedMedicineResponse>>

    fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>>

    fun isInterestedMedicine(itemSeq: Long): Flow<Result<IsInterestedMedicineResponse>>
}