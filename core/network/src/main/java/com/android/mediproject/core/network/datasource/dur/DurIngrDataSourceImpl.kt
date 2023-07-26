package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.datagokr.duringr.capacity.DurIngrCapacityAttentionResponse
import com.android.mediproject.core.model.datagokr.duringr.combination.DurIngrCombinationTabooResponse
import com.android.mediproject.core.model.datagokr.duringr.dosing.DurIngrDosingCautionResponse
import com.android.mediproject.core.model.datagokr.duringr.pregnancy.DurIngrPregnantWomanTabooResponse
import com.android.mediproject.core.model.datagokr.duringr.senior.DurIngrSeniorCautionResponse
import com.android.mediproject.core.model.datagokr.duringr.specialtyagegroup.DurIngrSpecialtyAgeGroupTabooResponse
import com.android.mediproject.core.network.module.datagokr.DurIngrInfoNetworkApi
import javax.inject.Inject

class DurIngrDataSourceImpl @Inject constructor(
    private val durIngrInfoNetworkApi: DurIngrInfoNetworkApi,
) : DurIngrDataSource {
    override suspend fun getCombinationTabooInfo(ingrKorName: String?, ingrCode: String?): Result<DurIngrCombinationTabooResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecialtyAgeGroupTabooInfo(ingrName: String?, ingrCode: String?): Result<DurIngrSpecialtyAgeGroupTabooResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPregnantWomanTabooInfo(ingrName: String?, ingrCode: String?): Result<DurIngrPregnantWomanTabooResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCapacityAttentionInfo(ingrName: String?, ingrCode: String?): Result<DurIngrCapacityAttentionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDosingCautionInfo(ingrName: String?, ingrCode: String?): Result<DurIngrDosingCautionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeniorCaution(ingrName: String?, ingrCode: String?): Result<DurIngrSeniorCautionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getEfficacyGroupDuplicationInfo(ingrName: String?, ingrCode: String?): Result<DurIngrCombinationTabooResponse> {
        TODO("Not yet implemented")
    }


}
