package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.model.toResult
import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSource
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.module.safetyEncode
import com.android.mediproject.core.network.onResponse
import com.android.mediproject.core.network.onStringResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicineApprovalDataSourceImpl @Inject constructor(
    private val dataGoKrNetworkApiWithString: DataGoKrNetworkApi,
    private val dataGoKrNetworkApiWithJson: DataGoKrNetworkApi,
    private val medicineDataCacheManager: MedicineDataCacheManager,
    private val googleSearchDataSource: GoogleSearchDataSource,
    private val defaultDispatcher: CoroutineDispatcher,
) : MedicineApprovalDataSource {

    override suspend fun getMedicineApprovalList(
        itemName: String?, entpName: String?, medicationType: String?, pageNo: Int,
    ): Result<MedicineApprovalListResponse> = dataGoKrNetworkApiWithJson.getApprovalList(
        itemName = itemName?.safetyEncode(), entpName = entpName?.safetyEncode(), pageNo = pageNo,
        medicationType = medicationType,
    ).onResponse().fold(
        onSuccess = { response ->
            response.toResult().fold(
                onSuccess = {
                    loadMedicineImageUrl(it)
                    Result.success(response)
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        },
        onFailure = {
            Result.failure(it)
        },
    )

    override fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse>> = channelFlow {
        dataGoKrNetworkApiWithString.getMedicineDetailInfo(itemName = itemName.safetyEncode()).let { response ->
            response.onStringResponse<MedicineDetailInfoResponse>().fold(
                onSuccess = { entity ->
                    //cache(entity.first, entity.second)
                    Result.success(entity.first)
                },
                onFailure = {
                    Result.failure(it)
                },
            ).also {
                send(it)
            }
        }
    }

    override fun getMedicineDetailInfoByItemSeq(itemSeqs: List<String>) = channelFlow {
        val responses = itemSeqs.map { itemSeq ->
            dataGoKrNetworkApiWithJson.getMedicineDetailInfo(itemSeq = itemSeq).let { response ->
                response.onStringResponse<MedicineDetailInfoResponse>().fold(
                    onSuccess = { entity ->
                        //cache(entity.first, entity.second)
                        Result.success(entity.first)
                    },
                    onFailure = {
                        Result.failure(it)
                    },
                )
            }
        }

        val result = responses.filter { it.isFailure }.run {
            if (isEmpty()) Result.success(responses.map { it.getOrNull()!! })
            else Result.failure(first().exceptionOrNull()!!)
        }

        send(result)
    }

    private fun cache(response: MedicineDetailInfoResponse, string: String) {
        /* val item = response.body.items.first()
         medicineDataCacheManager.updateDetail(
             MedicineCacheEntity(
                 itemSequence = item.itemSequence,
                 json = WeakReference(string).get()!!,
                 changeDate = item.changeDate,
             ),
         )*/
    }

    private suspend fun loadMedicineImageUrl(medicineApprovalListResponse: MedicineApprovalListResponse) {
        return withContext(defaultDispatcher) {
            val items = mutableListOf<Pair<Int, String>>()
            medicineApprovalListResponse.body.items.forEachIndexed { index, item ->
                if (item.bigPrdtImgUrl.isEmpty()) items.add(index to item.itemName)
            }
            if (items.isEmpty()) return@withContext

            val map = googleSearchDataSource.fetchImageUrls(items.map { it.second }, "의약품 ")
            medicineApprovalListResponse.body.items.run {
                items.forEach { (i, seq) ->
                    this[i].bigPrdtImgUrl = map.getOrDefault(seq, "")
                }
            }
        }
    }
}
