package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.datagokr.duringr.capacity.DurIngrCapacityAttentionResponse
import com.android.mediproject.core.model.datagokr.duringr.combination.DurIngrCombinationTabooResponse
import com.android.mediproject.core.model.datagokr.duringr.dosing.DurIngrDosingCautionResponse
import com.android.mediproject.core.model.datagokr.duringr.pregnancy.DurIngrPregnantWomanTabooResponse
import com.android.mediproject.core.model.datagokr.duringr.senior.DurIngrSeniorCautionResponse
import com.android.mediproject.core.model.datagokr.duringr.specialtyagegroup.DurIngrSpecialtyAgeGroupTabooResponse

interface DurIngrDataSource {
    suspend fun getCombinationTabooInfo(
        ingrKorName: String?,
        ingrCode: String?,
    ): Result<DurIngrCombinationTabooResponse>


    suspend fun getSpecialtyAgeGroupTabooInfo(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrSpecialtyAgeGroupTabooResponse>

    suspend fun getPregnantWomanTabooInfo(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrPregnantWomanTabooResponse>

    suspend fun getCapacityAttentionInfo(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrCapacityAttentionResponse>

    suspend fun getDosingCautionInfo(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrDosingCautionResponse>


    suspend fun getSeniorCaution(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrSeniorCautionResponse>

    suspend fun getEfficacyGroupDuplicationInfo(
        ingrName: String?,
        ingrCode: String?,
    ): Result<DurIngrCombinationTabooResponse>
}
