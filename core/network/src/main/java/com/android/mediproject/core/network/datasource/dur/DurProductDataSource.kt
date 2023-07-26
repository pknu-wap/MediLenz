package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.combination.DurProductCombinationTabooResponse
import com.android.mediproject.core.model.dur.durproduct.dosing.DurProductDosingCautionResponse
import com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationResponse
import com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet.DurProductExReleaseTableSplitAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.pregnancy.DurProductPregnantWomanTabooResponse
import com.android.mediproject.core.model.dur.durproduct.productlist.DurProductListResponse
import com.android.mediproject.core.model.dur.durproduct.senior.DurProductSeniorCautionResponse
import com.android.mediproject.core.model.dur.durproduct.specialtyagegroup.DurProductSpecialtyAgeGroupTabooResponse
import kotlinx.coroutines.flow.Flow

interface DurProductDataSource {

    fun getDurList(itemSeq: String, durTypes: List<DurType>): Flow<Map<DurType, Result<DataGoKrResponse<*>>>>

    suspend fun getDurProductList(
        itemName: String?,
        itemSeq: String?,
    ): Result<DurProductListResponse>

    suspend fun getSeniorCaution(
        itemName: String?,
        ingrCode: String?,
        itemSeq: String?,
    ): Result<DurProductSeniorCautionResponse>

    suspend fun getExReleaseTableSplitAttentionInfo(
        itemName: String?,
        entpName: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductExReleaseTableSplitAttentionResponse>

    suspend fun getEfficacyGroupDuplicationInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductEfficacyGroupDuplicationResponse>

    suspend fun getDosingCautionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductDosingCautionResponse>

    suspend fun getCapacityAttentionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCapacityAttentionResponse>

    suspend fun getPregnantWomanTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductPregnantWomanTabooResponse>

    suspend fun getSpecialtyAgeGroupTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductSpecialtyAgeGroupTabooResponse>

    suspend fun getCombinationTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCombinationTabooResponse>
}
