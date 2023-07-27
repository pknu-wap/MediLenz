package com.android.mediproject.core.domain

import com.android.mediproject.core.data.dur.durproduct.DurProductRepository
import com.android.mediproject.core.model.dur.DurType
import kotlinx.coroutines.flow.last
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDurUseCase @Inject constructor(
    private val durProductRepository: DurProductRepository,
    private val durIngrRepository: DurProductRepository,
) {
    suspend fun hasDur(
        itemName: String?, itemSeq: String?,
    ) = durProductRepository.hasDur(itemName, itemSeq).last()

    suspend fun getDur(
        itemSeq: String, durTypes: List<DurType>,
    ) = durProductRepository.getDur(itemSeq, durTypes).last()
}
