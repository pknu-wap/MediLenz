package com.android.mediproject.core.data.remote.dur

import com.android.mediproject.core.network.datasource.dur.DurDataSource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DurRepositoryImpl @Inject constructor(
    private val dataSource: DurDataSource
) : DurRepository {
    override suspend fun getDur(itemName: String?, itemSeq: String?) = channelFlow {
        dataSource.getDur(itemName = itemName, itemSeq = itemSeq).map { result ->
            result.fold(onSuccess = { Result.success(it.body.items.first()) }, onFailure = { Result.failure(it) })
        }.collectLatest {
            send(it)
        }
    }
}