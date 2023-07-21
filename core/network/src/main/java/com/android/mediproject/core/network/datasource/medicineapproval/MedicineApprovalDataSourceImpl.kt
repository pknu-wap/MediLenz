package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.model.DataGoKrResult
import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity
import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSource
import com.android.mediproject.core.network.module.DataGoKrNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.ref.WeakReference
import javax.inject.Inject

class MedicineApprovalDataSourceImpl @Inject constructor(
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
    private val medicineDataCacheManager: MedicineDataCacheManager,
    private val googleSearchDataSource: GoogleSearchDataSource,
) : MedicineApprovalDataSource {

    override suspend fun getMedicineApprovalList(
        itemName: String?, entpName: String?, medicationType: String?, pageNo: Int,
    ): Result<MedicineApprovalListResponse> =
        dataGoKrNetworkApi.getApprovalList(itemName = itemName, entpName = entpName, pageNo = pageNo, medicationType = medicationType).onResponse()
            .fold(
                onSuccess = { response ->
                    response.isSuccess().let {
                        if (it == DataGoKrResult.isSuccess) {
                            // 이미지가 없는 경우 구글 검색을 통해 이미지를 가져온다.
                            loadMedicineImageUrl(response)
                            Result.success(response)
                        } else Result.failure(Throwable(it.failedMessage))
                    }
                },
                onFailure = {
                    Result.failure(it)
                },
            )

    override fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse>> = channelFlow {
        dataGoKrNetworkApi.getMedicineDetailInfo(itemName = itemName).let { response ->
            response.onResponse().fold(
                onSuccess = { entity ->
                    entity.isSuccess().let {
                        if (it is DataGoKrResult.isSuccess) {
                            cache(response.body()!!)
                            Result.success(entity)
                        } else Result.failure(Throwable(it.failedMessage))
                    }
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
            dataGoKrNetworkApi.getMedicineDetailInfo(itemSeq = itemSeq).let { response ->
                response.onResponse().fold(
                    onSuccess = { entity ->
                        entity.isSuccess().let {
                            if (it is DataGoKrResult.isSuccess) {
                                cache(response.body()!!)
                                Result.success(entity)
                            } else Result.failure(Throwable(it.failedMessage))
                        }
                    },
                    onFailure = {
                        Result.failure(it)
                    },
                )
            }
        }

        val results = responses.let {
            val failed = it.any { result -> result.isFailure }
            if (failed) Result.failure(Throwable("약품 상세 정보 조회에 실패했습니다."))
            else Result.success(it.map { result -> result.getOrNull()!! })
        }

        trySend(results)
    }

    private fun cache(response: MedicineDetailInfoResponse) {
        with(response.body.items[0]) {
            medicineDataCacheManager.updateDetail(
                MedicineCacheEntity(
                    itemSequence = itemSequence,
                    json = WeakReference(Json.encodeToString(response.body)).get()!!,
                    changeDate = changeDate,
                ),
            )
        }
    }

    private suspend fun loadMedicineImageUrl(medicineApprovalListResponse: MedicineApprovalListResponse) {
        return withContext(Dispatchers.Default) {
            val items = mutableListOf<Pair<Int, String>>()
            medicineApprovalListResponse.body.items.forEachIndexed { index, item ->
                if (item.bigPrdtImgUrl.isEmpty()) items.add(index to item.itemName)
            }
            if (items.isEmpty()) return@withContext

            val map = mutableMapOf<String, String>()
            val asyncList = items.map { (i, name) ->
                async {
                    val imageUrl = googleSearchDataSource.getImageUrl(name)
                    synchronized(map) {
                        map[name] = imageUrl.getOrDefault("")
                    }
                }
            }

            asyncList.forEach { it.await() }
            medicineApprovalListResponse.body.items.run {
                items.forEach { (i, seq) ->
                    this[i].bigPrdtImgUrl = map.getOrDefault(seq, "")
                }
            }
        }
    }
}
