package com.android.mediproject.core.network.datasource.dur

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.combination.DurProductCombinationTabooResponse
import com.android.mediproject.core.model.dur.durproduct.dosing.DurProductDosingCautionResponse
import com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationResponse
import com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet.DurProductExReleaseTabletSplitAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.pregnancy.DurProductPregnantWomanTabooResponse
import com.android.mediproject.core.model.dur.durproduct.productlist.DurProductListResponse
import com.android.mediproject.core.model.dur.durproduct.senior.DurProductSeniorCautionResponse
import com.android.mediproject.core.model.dur.durproduct.specialtyagegroup.DurProductSpecialtyAgeGroupTabooResponse
import com.android.mediproject.core.network.module.datagokr.DurProductInfoNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class DurProductDataSourceImpl @Inject constructor(
    private val durProductInfoNetworkApi: DurProductInfoNetworkApi,
) : DurProductDataSource {

    override fun getDurList(itemSeq: String, durTypes: List<DurType>): Flow<Map<DurType, Result<DataGoKrResponse<out Any>>>> = channelFlow {
        val map: Map<DurType, Result<DataGoKrResponse<out Any>>> = durTypes.map { type ->
            type to async { call(type, itemSeq) }
        }.associate { it.first to it.second.await() }
        send(map)
    }


    private suspend inline fun call(durType: DurType, itemSeq: String): Result<DataGoKrResponse<out Any>> = when (durType) {
        DurType.EX_RELEASE_TABLET_SPLIT_ATTENTION -> getExReleaseTableSplitAttentionInfo(
            itemName = null, entpName = null,
            typeName = null, itemSeq = itemSeq,
        )

        DurType.EFFICACY_GROUP_DUPLICATION -> getEfficacyGroupDuplicationInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.DOSING_CAUTION -> getDosingCautionInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.CAPACITY_ATTENTION -> getCapacityAttentionInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.PREGNANT_WOMAN_TABOO -> getPregnantWomanTabooInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.SPECIALTY_AGE_GROUP_TABOO -> getSpecialtyAgeGroupTabooInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.COMBINATION_TABOO -> getCombinationTabooInfo(itemName = null, ingrCode = null, typeName = null, itemSeq = itemSeq)
        DurType.SENIOR_CAUTION -> getSeniorCaution(itemName = null, ingrCode = null, itemSeq = itemSeq)
    }

    override suspend fun getDurProductList(itemName: String?, itemSeq: String?): Result<DurProductListResponse> =
        durProductInfoNetworkApi.getDurProductList(itemName = itemName, itemSeq = itemSeq).onDataGokrResponse()


    override suspend fun getSeniorCaution(itemName: String?, ingrCode: String?, itemSeq: String?): Result<DurProductSeniorCautionResponse> =
        durProductInfoNetworkApi.getSeniorCaution(itemName = itemName, ingrCode = ingrCode, itemSeq = itemSeq).onDataGokrResponse()

    override suspend fun getExReleaseTableSplitAttentionInfo(
        itemName: String?,
        entpName: String?,
        typeName: String?,
        itemSeq: String?,
    ): Result<DurProductExReleaseTabletSplitAttentionResponse> =
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
