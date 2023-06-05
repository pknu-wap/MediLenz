package com.android.mediproject.core.network.datasource.interestedmedicine

import com.android.mediproject.core.model.medicine.InterestedMedicine.InterestedMedicineListResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class InterestedMedicineDataSourceImpl @Inject constructor(private val awsNetworkApi: AwsNetworkApi) :
    InterestedMedicineDataSource {
    override suspend fun getInterestedMedicineList(): Flow<Result<InterestedMedicineListResponse>> =
        channelFlow {
            awsNetworkApi.getInterestedMedicineList().onResponse()
                .fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) }).also {
                    trySend(it)
                }
        }
}