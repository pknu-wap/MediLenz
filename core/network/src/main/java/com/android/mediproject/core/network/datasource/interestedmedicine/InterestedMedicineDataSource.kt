package com.android.mediproject.core.network.datasource.interestedmedicine

import com.android.mediproject.core.model.remote.medicine.InterestedMedicineListResponse
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineDataSource {
    suspend fun getInterestedMedicineList() : Flow<Result<InterestedMedicineListResponse>>
}