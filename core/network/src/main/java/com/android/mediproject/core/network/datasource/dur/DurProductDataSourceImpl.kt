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
import com.android.mediproject.core.network.module.datagokr.DurProductInfoNetworkApi
import javax.inject.Inject

class DurProductDataSourceImpl @Inject constructor(
    private val durProductInfoNetworkApi: DurProductInfoNetworkApi,
) : DurProductDataSource {
    override suspend fun getDurProductList(itemName: String?, itemSeq: String?): Result<DurProductListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeniorCaution(itemName: String?, ingrCode: String?, itemSeq: String?): Result<DurProductSeniorCautionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getExReleaseTableSplitAttentionInfo(
        itemName: String?,
        entpName: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductExReleaseTableSplitAttentionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getEfficacyGroupDuplicationInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductEfficacyGroupDuplicationResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDosingCautionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductDosingCautionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCapacityAttentionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCapacityAttentionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPregnantWomanTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductPregnantWomanTabooResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecialtyAgeGroupTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductSpecialtyAgeGroupTabooResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCombinationTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCombinationTabooResponse> {
        TODO("Not yet implemented")
    }


}
