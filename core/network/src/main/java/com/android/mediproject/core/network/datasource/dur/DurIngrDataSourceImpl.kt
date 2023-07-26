package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.dur.duringr.capacity.DurIngrCapacityAttentionResponse
import com.android.mediproject.core.model.dur.duringr.combination.DurIngrCombinationTabooResponse
import com.android.mediproject.core.model.dur.duringr.dosing.DurIngrDosingCautionResponse
import com.android.mediproject.core.model.dur.duringr.pregnancy.DurIngrPregnantWomanTabooResponse
import com.android.mediproject.core.model.dur.duringr.senior.DurIngrSeniorCautionResponse
import com.android.mediproject.core.model.dur.duringr.specialtyagegroup.DurIngrSpecialtyAgeGroupTabooResponse
import com.android.mediproject.core.network.module.datagokr.DurIngrInfoNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import javax.inject.Inject

class DurIngrDataSourceImpl @Inject constructor(
    private val durIngrInfoNetworkApi: DurIngrInfoNetworkApi,
) : DurIngrDataSource {
    override suspend fun getCombinationTabooInfo(ingrKorName: String?, ingrCode: String?): Result<DurIngrCombinationTabooResponse> =
        durIngrInfoNetworkApi.getCombinationTabooInfo(ingrKorName = ingrKorName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getSpecialtyAgeGroupTabooInfo(ingrName: String?, ingrCode: String?): Result<DurIngrSpecialtyAgeGroupTabooResponse> =
        durIngrInfoNetworkApi.getSpecialtyAgeGroupTabooInfo(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getPregnantWomanTabooInfo(ingrName: String?, ingrCode: String?): Result<DurIngrPregnantWomanTabooResponse> =
        durIngrInfoNetworkApi.getPregnantWomanTabooInfo(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getCapacityAttentionInfo(ingrName: String?, ingrCode: String?): Result<DurIngrCapacityAttentionResponse> =
        durIngrInfoNetworkApi.getCapacityAttentionInfo(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getDosingCautionInfo(ingrName: String?, ingrCode: String?): Result<DurIngrDosingCautionResponse> =
        durIngrInfoNetworkApi.getDosingCautionInfo(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getSeniorCaution(ingrName: String?, ingrCode: String?): Result<DurIngrSeniorCautionResponse> =
        durIngrInfoNetworkApi.getSeniorCaution(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

    override suspend fun getEfficacyGroupDuplicationInfo(ingrName: String?, ingrCode: String?): Result<DurIngrCombinationTabooResponse> =
        durIngrInfoNetworkApi.getEfficacyGroupDuplicationInfo(ingrName = ingrName, ingrCode = ingrCode).onDataGokrResponse()

}
