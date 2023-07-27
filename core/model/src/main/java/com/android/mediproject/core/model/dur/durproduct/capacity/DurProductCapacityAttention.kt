package com.android.mediproject.core.model.dur.durproduct.capacity

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 용량주의
 *
 * @param itemName 품목명
 * @param prohibitContent 1일 최대투여량
 */
data class DurProductCapacityAttention(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.CAPACITY_ATTENTION)


fun DurProductCapacityAttentionResponse.Item.toDurProductCapacityAttention() = DurProductCapacityAttention(
    itemName = itemName,
    prohibitContent = prohibitContent,
)

class DurProductCapacityAttentionWrapper(
    override val response: DurProductCapacityAttentionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductCapacityAttention(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }
}
