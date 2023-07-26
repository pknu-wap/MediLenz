package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.datagokr.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.model.datagokr.durproduct.combination.DurProductCombinationTabooResponse
import com.android.mediproject.core.model.datagokr.durproduct.dosing.DurProductDosingCautionResponse
import com.android.mediproject.core.model.datagokr.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationResponse
import com.android.mediproject.core.model.datagokr.durproduct.extendedreleasetablet.DurProductExReleaseTableSplitAttentionResponse
import com.android.mediproject.core.model.datagokr.durproduct.pregnancy.DurProductPregnantWomanTabooResponse
import com.android.mediproject.core.model.datagokr.durproduct.productlist.DurProductListResponse
import com.android.mediproject.core.model.datagokr.durproduct.senior.DurProductSeniorCautionResponse
import com.android.mediproject.core.model.datagokr.durproduct.specialtyagegroup.DurProductSpecialtyAgeGroupTabooResponse

interface DurProductDataSource {
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
