package com.android.mediproject.core.network.datasource.interestedmedicine

import com.android.mediproject.core.model.interestedmedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.interestedmedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.interestedmedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.interestedmedicine.NewInterestedMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class InterestedMedicineDataSourceImpl @Inject constructor(private val awsNetworkApi: AwsNetworkApi) : InterestedMedicineDataSource {
    override suspend fun getInterestedMedicineList(): Flow<Result<InterestedMedicineListResponse>> = channelFlow {
        awsNetworkApi.getInterestedMedicineList().onResponse().fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
            .also {
                trySend(it)
            }
    }

    override fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<NewInterestedMedicineResponse>> =
        channelFlow {
            awsNetworkApi.addInterestedMedicine(addInterestedMedicineParameter).onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                    trySend(it)
                }
        }

    override fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>> = channelFlow {
        awsNetworkApi.deleteInterestedMedicine(medicineId).onResponse()
            .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                trySend(it)
            }
    }

    override fun isInterestedMedicine(medicineId: Long): Flow<Result<IsInterestedMedicineResponse>> = channelFlow {
        awsNetworkApi.isInterestedMedicine(medicineId).onResponse()
            .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                trySend(it)
            }
    }
}