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
import com.android.mediproject.core.network.onDataGokrResponse
import javax.inject.Inject

class DurProductDataSourceImpl @Inject constructor(
    private val durProductInfoNetworkApi: DurProductInfoNetworkApi,
) : DurProductDataSource {
    override suspend fun getDurProductList(itemName: String?, itemSeq: String?): Result<DurProductListResponse> =
        durProductInfoNetworkApi.getDurProductList(itemName = itemName, itemSeq = itemSeq).onDataGokrResponse()

    override suspend fun getSeniorCaution(itemName: String?, ingrCode: String?, itemSeq: String?): Result<DurProductSeniorCautionResponse> =
        durProductInfoNetworkApi.getSeniorCaution(itemName = itemName, ingrCode = ingrCode, itemSeq = itemSeq).onDataGokrResponse()

    override suspend fun getExReleaseTableSplitAttentionInfo(
        itemName: String?,
        entpName: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductExReleaseTableSplitAttentionResponse> =
        durProductInfoNetworkApi.getExReleaseTableSplitAttentionInfo(itemName = itemName, entpName = entpName, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getEfficacyGroupDuplicationInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductEfficacyGroupDuplicationResponse> =
        durProductInfoNetworkApi.getEfficacyGroupDuplicationInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getDosingCautionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductDosingCautionResponse> =
        durProductInfoNetworkApi.getDosingCautionInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getCapacityAttentionInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCapacityAttentionResponse> =
        durProductInfoNetworkApi.getCapacityAttentionInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getPregnantWomanTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductPregnantWomanTabooResponse> =
        durProductInfoNetworkApi.getPregnantWomanTabooInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getSpecialtyAgeGroupTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductSpecialtyAgeGroupTabooResponse> =
        durProductInfoNetworkApi.getSpecialtyAgeGroupTabooInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

    override suspend fun getCombinationTabooInfo(
        itemName: String?,
        ingrCode: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductCombinationTabooResponse> =
        durProductInfoNetworkApi.getCombinationTabooInfo(itemName = itemName, ingrCode = ingrCode, typeName = typeName, itemSeq = itemSeq)
            .onDataGokrResponse()

}
