package com.android.mediproject.core.data.dur.durproduct

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.core.model.dur.DurItemWrapperFactory
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.network.datasource.dur.DurProductDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class DurProductRepositoryImpl @Inject constructor(
    private val durProductDataSource: DurProductDataSource,
) : DurProductRepository {

    private val mapCacheMaxSize = 5
    private val durProductCacheMap = mutableMapOf<String, MutableMap<DurType, DataGoKrResponse<*>>>()
    private val mutex = Mutex()

    override fun hasDur(itemName: String?, itemSeq: String?): Flow<Result<List<DurType>>> = channelFlow {
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

    override fun getDur(itemSeq: String, durTypes: List<DurType>): Flow<Map<DurType, Result<List<DurItem>>>> = channelFlow {
        val filteredDurTypes = mutex.withLock {
            durProductCacheMap[itemSeq]?.keys?.filter { it !in durTypes } ?: durTypes
        }
        val map = mutableMapOf<DurType, Result<List<DurItem>>>()
        durProductDataSource.getDurList(itemSeq, filteredDurTypes).last().forEach { (durType, response) ->
            response.onSuccess {
                cache(itemSeq, durType, it)
                durType to Result.success(DurItemWrapperFactory.createForDurProduct(durType, it).convert())
            }.onFailure {
                durType to Result.failure<List<DurItem>>(it)
            }
        }

        send(map)
    }

    private suspend fun cache(key: String, durType: DurType, response: DataGoKrResponse<*>) {
        mutex.withLock {
            if (durProductCacheMap.size > mapCacheMaxSize) durProductCacheMap.remove(durProductCacheMap.keys.first())
            durProductCacheMap[key] = durProductCacheMap[key]?.apply { this[durType] = response } ?: mutableMapOf(durType to response)
        }
    }
}
