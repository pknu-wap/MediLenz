package com.android.mediproject.core.network.datasource.favoritemedicine

import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import kotlinx.coroutines.flow.Flow

interface FavoriteMedicineDataSource {
    suspend fun getFavoriteMedicineList(): Flow<Result<FavoriteMedicineListResponse>>

    fun addFavoriteMedicine(addFavoriteMedicineParameter: AddFavoriteMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>>

    fun deleteFavoriteMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>>

    fun checkFavoriteMedicine(medicineId: Long): Flow<Result<CheckFavoriteMedicineResponse>>
}