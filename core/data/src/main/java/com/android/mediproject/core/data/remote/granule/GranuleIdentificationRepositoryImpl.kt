package com.android.mediproject.core.data.remote.granule

import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GranuleIdentificationRepositoryImpl @Inject constructor(
    private val dataSource: GranuleIdentificationDataSource) : GranuleIdentificationRepository {


    override fun getGranuleIdentificationInfo(
        itemName: String?, entpName: String?, itemSeq: String?) =
        dataSource.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).flatMapLatest { result ->
            result.fold(onSuccess = { response ->
                flowOf(Result.success(response.body!!.items!!.first()))
            }, onFailure = {
                flowOf(Result.failure(it))
            })
        }
}