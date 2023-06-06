package com.android.mediproject.core.network.datasource.medicineid

import com.android.mediproject.core.model.medicine.MedicineIdResponse
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MedicineIdDataSourceImpl @Inject constructor(private val awsNetworkApi: AwsNetworkApi) : MedicineIdDataSource {
    override fun getMedicineId(getMedicineIdParameter: GetMedicineIdParameter): Flow<Result<MedicineIdResponse>> = flow {
        awsNetworkApi.getMedicineId(getMedicineIdParameter).onResponse().fold(onSuccess = { response ->
            Result.success(response)
        }, onFailure = {
            Result.failure(it)
        }).also { emit(it) }
    }

}