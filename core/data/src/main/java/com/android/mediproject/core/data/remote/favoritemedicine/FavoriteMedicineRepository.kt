package com.android.mediproject.core.data.remote.favoritemedicine


import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import kotlinx.coroutines.flow.Flow

interface FavoriteMedicineRepository {
    suspend fun getFavoriteMedicineList(): Flow<Result<List<FavoriteMedicineListResponse.Medicine>>>

    fun addFavortieMedicine(addFavoriteMedicineParameter: AddFavoriteMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>>

    fun deleteFavoriteMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>>

    fun checkFavoriteMedicine(itemSeq: Long): Flow<Result<CheckFavoriteMedicineResponse>>
}