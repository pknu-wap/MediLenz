package com.android.mediproject.core.data.remote.interestedmedicine

import com.android.mediproject.core.model.medicine.interestedMedicine.AddInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import com.android.mediproject.core.network.datasource.interestedmedicine.InterestedMedicineDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class InterestedMedicineRepositoryImpl @Inject constructor(private val interestedMedicineDataSource: InterestedMedicineDataSource) :
    InterestedMedicineRepository {
    override suspend fun getInterestedMedicineList(): Flow<Result<List<InterestedMedicineListResponse.Medicine>>> = channelFlow {
        interestedMedicineDataSource.getInterestedMedicineList().map { result ->
            result.fold(onSuccess = { Result.success(it.medicineList) }, onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    override fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<AddInterestedMedicineResponse>> =
        interestedMedicineDataSource.addInterestedMedicine(addInterestedMedicineParameter)

    override fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>> =
        interestedMedicineDataSource.deleteInterestedMedicine(medicineId)

    override fun isInterestedMedicine(itemSeq: Long): Flow<Result<IsInterestedMedicineResponse>> =
        interestedMedicineDataSource.isInterestedMedicine(itemSeq)
}