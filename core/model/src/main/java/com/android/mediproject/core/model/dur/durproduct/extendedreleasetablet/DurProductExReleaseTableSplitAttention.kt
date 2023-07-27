package com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 서방정 분할 주의
 *
 * @param itemName 품목명
 * @param prohibitContent 분할금기 내용
 */
data class DurProductExReleaseTableSplitAttention(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.EX_RELEASE_TABLET_SPLIT_ATTENTION)

class DurProductExReleaseTableSplitAttentionWrapper(
    override val response: DurProductExReleaseTableSplitAttentionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductExReleaseTableSplitAttention(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }
}
