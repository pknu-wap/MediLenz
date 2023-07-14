package com.android.mediproject.core.network.datasource.favoritemedicine

import com.android.mediproject.core.model.favoritemedicine.DeleteFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineListResponse
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.NewFavoriteMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class FavoriteMedicineDataSourceImpl @Inject constructor(private val awsNetworkApi: AwsNetworkApi) :
    FavoriteMedicineDataSource {
    override suspend fun getFavoriteMedicineList(): Flow<Result<FavoriteMedicineListResponse>> =
        channelFlow {
            awsNetworkApi.getFavoriteMedicineList().onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
                .also {
                    trySend(it)
                }
        }

    override fun addFavoriteMedicine(addFavoriteMedicineParameter: AddFavoriteMedicineParameter): Flow<Result<NewFavoriteMedicineResponse>> =
        channelFlow {
            awsNetworkApi.addFavoriteMedicine(addFavoriteMedicineParameter).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                    trySend(it)
                }
        }

    override fun deleteFavoriteMedicine(medicineId: Long): Flow<Result<DeleteFavoriteMedicineResponse>> =
        channelFlow {
            awsNetworkApi.deleteFavoriteMedicine(medicineId).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                    trySend(it)
                }
        }

    override fun checkFavoriteMedicine(medicineId: Long): Flow<Result<CheckFavoriteMedicineResponse>> =
        channelFlow {
            awsNetworkApi.checkFavoriteMedicine(medicineId).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                    trySend(it)
                }
        }
}