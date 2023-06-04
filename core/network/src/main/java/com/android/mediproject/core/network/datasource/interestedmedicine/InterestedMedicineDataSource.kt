package com.android.mediproject.core.network.datasource.interestedmedicine

import kotlinx.coroutines.flow.Flow

interface InterestedMedicineDataSource {
    suspend fun getInterestedMedicineList() : Flow<Result<InterestedMedicineListResponse>>
}