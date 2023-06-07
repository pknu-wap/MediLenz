package com.android.mediproject.core.network.datasource.interestedmedicine

import com.android.mediproject.core.model.medicine.interestedMedicine.AddInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.DeleteInterestedMedicineResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.InterestedMedicineListResponse
import com.android.mediproject.core.model.medicine.interestedMedicine.IsInterestedMedicineResponse
import com.android.mediproject.core.model.requestparameters.AddInterestedMedicineParameter
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineDataSource {
    suspend fun getInterestedMedicineList(): Flow<Result<InterestedMedicineListResponse>>

    /**
     * 관심 약 추가
     */
    fun addInterestedMedicine(addInterestedMedicineParameter: AddInterestedMedicineParameter): Flow<Result<AddInterestedMedicineResponse>>

    /**
     * 관심 약 삭제
     */
    fun deleteInterestedMedicine(medicineId: Long): Flow<Result<DeleteInterestedMedicineResponse>>

    /**
     * 관심 약 여부 확인
     */
    fun isInterestedMedicine(medicineId: Long): Flow<Result<IsInterestedMedicineResponse>>
}