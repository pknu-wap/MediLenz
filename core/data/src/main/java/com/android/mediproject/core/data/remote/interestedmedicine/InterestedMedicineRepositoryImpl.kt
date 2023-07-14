package com.android.mediproject.core.data.remote.interestedmedicine

import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import com.android.mediproject.core.network.datasource.favoritemedicine.FavoriteMedicineDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class InterestedMedicineRepositoryImpl @Inject constructor(private val favoriteMedicineDataSource: FavoriteMedicineDataSource) :
    InterestedMedicineRepository {
    override suspend fun getInterestedMedicineList(): Flow<Result<List<FavoriteMedicineListResponse.Medicine>>> = channelFlow {
        favoriteMedicineDataSource.getFavoriteMedicineList().map { result ->
            result.fold(onSuccess = { Result.success(it.medicineList) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    override fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.addFavoriteMedicine(addInterestedMedicineParameter)

    override fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.deleteFavoriteMedicine(medicineId)

    override fun isInterestedMedicine(itemSeq: Long): Flow<Result<CheckFavoriteMedicineResponse>> =
        favoriteMedicineDataSource.checkFavoriteMedicine(itemSeq)
}