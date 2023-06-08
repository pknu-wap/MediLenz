package com.android.mediproject.core.data.remote.interestedmedicine


import com.android.mediproject.core.model.medicine.interestedmedicine.InterestedMedicineListResponse
import com.android.mediproject.core.network.datasource.interestedmedicine.InterestedMedicineDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class InterestedMedicineRepositoryImpl @Inject constructor(private val interestedMedicineDataSource: InterestedMedicineDataSource) :
    InterestedMedicineRepository {
    override suspend fun getInterestedMedicineList(): Flow<Result<List<InterestedMedicineListResponse.Medicine>>> =
        channelFlow {
            interestedMedicineDataSource.getInterestedMedicineList().map { result ->
                result.fold(
                    onSuccess = { Result.success(it.medicineList) },
                    onFailure = { Result.failure(it) })
            }.collectLatest { trySend(it) }
        }
}