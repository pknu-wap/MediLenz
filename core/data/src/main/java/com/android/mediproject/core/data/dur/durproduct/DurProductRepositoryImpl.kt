package com.android.mediproject.core.data.dur.durproduct

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.datagokr.DurType
import com.android.mediproject.core.network.datasource.dur.DurProductDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class DurProductRepositoryImpl @Inject constructor(
    private val durProductDataSource: DurProductDataSource,
) : DurProductRepository {

    private val mapCacheMaxSize = 5
    private val _durProductCacheMap = mutableMapOf<String, Map<DurType, DataGoKrResponse<*>>>()
    private val durProductCacheMap = _durProductCacheMap.toMap()
    private val mutex = Mutex()

    fun hasDur(itemName: String?, itemSeq: String?): Flow<Result<List<DurType>>> = channelFlow {
        val response = durProductDataSource.getDurProductList(itemName = itemName, itemSeq = itemSeq)
        response.onSuccess {
            if (it.body.items.isNotEmpty()) {
                val durTypeList = it.body.items.map { item -> DurType.valueOf(item.typeName) }
                send(Result.success(durTypeList))
            } else {
                send(Result.failure(Throwable("No Dur")))
            }
        }.onFailure {
            send(Result.failure(it))
        }
    }

    fun durList(itemName: String?, itemSeq: String?): Flow<Map<DurType, DataGoKrResponse<*>>> = channelFlow {

    }

    private suspend fun cache(key: String, durType: DurType, response: DataGoKrResponse<*>) {
        mutex.withLock {
            if (_durProductCacheMap.size > mapCacheMaxSize) _durProductCacheMap.remove(_durProductCacheMap.keys.first())
            _durProductCacheMap[key] = _durProductCacheMap[key]?.plus(durType to response) ?: mapOf(durType to response)
        }
    }
}
