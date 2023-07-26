package com.android.mediproject.core.data.favoritemedicine

import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import com.android.mediproject.core.network.datasource.favoritemedicine.FavoriteMedicineDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FavoriteMedicineRepositoryImpl @Inject constructor(private val favoriteMedicineDataSource: FavoriteMedicineDataSource) :
    FavoriteMedicineRepository {
    override suspend fun getFavoriteMedicineList(): Flow<Result<List<FavoriteMedicineListResponse.Medicine>>> = channelFlow {
        favoriteMedicineDataSource.getFavoriteMedicineList().map { result ->
            result.fold(onSuccess = { Result.success(it.medicineList) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    override fun addFavortieMedicine(addFavoriteMedicineParameter: AddFavoriteMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.addFavoriteMedicine(addFavoriteMedicineParameter)

    override fun deleteFavoriteMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.deleteFavoriteMedicine(medicineId)

    override fun checkFavoriteMedicine(itemSeq: Long): Flow<Result<CheckFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.checkFavoriteMedicine(itemSeq)
}
