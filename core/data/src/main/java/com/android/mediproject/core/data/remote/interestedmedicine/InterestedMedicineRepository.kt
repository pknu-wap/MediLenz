package com.android.mediproject.core.data.remote.interestedmedicine


import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineRepository {
    suspend fun getInterestedMedicineList(): Flow<Result<List<FavoriteMedicineListResponse.Medicine>>>

    fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>>

    fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>>

    fun isInterestedMedicine(itemSeq: Long): Flow<Result<CheckFavoriteMedicineResponse>>
}