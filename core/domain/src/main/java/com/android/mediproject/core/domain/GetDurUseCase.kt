package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.dur.DurRepository
import com.android.mediproject.core.model.datagokr.durproduct.productlist.toDto
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDurUseCase @Inject constructor(private val durRepository: DurRepository) {

    suspend operator fun invoke(
        itemName: String?,
        itemSeq: String?,
    ) = channelFlow {
        durRepository.getDur(itemName, itemSeq).map { result ->
            result.fold(
                onSuccess = {
                    Result.success(it.toDto())
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        }.collectLatest {
            send(it)
        }
    }
}
