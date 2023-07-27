package com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet

import android.text.Spanned
import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem
import com.android.mediproject.core.model.dur.toHtml

/**
 * 서방정 분할 주의
 *
 * @param itemName 품목명
 * @param prohibitContent 분할금기 내용
 */
data class DurProductExReleaseTabletSplitAttention(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.EX_RELEASE_TABLET_SPLIT_ATTENTION) {

    override val content: Spanned = "<b>$itemName<b>\n$prohibitContent".toHtml()
}

class DurProductExReleaseTableSplitAttentionWrapper(
    override val response: DurProductExReleaseTabletSplitAttentionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductExReleaseTabletSplitAttention(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }
}
