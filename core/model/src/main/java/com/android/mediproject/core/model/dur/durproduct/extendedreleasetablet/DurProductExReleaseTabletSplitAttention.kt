package com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.text.toSpanned
import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem
import java.lang.ref.WeakReference

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

    override val content: Spanned
        get() = WeakReference(SpannableStringBuilder()).get()!!.let { builder ->
            builder.append(itemName)
            builder.setSpan(StyleSpan(Typeface.BOLD), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.setSpan(RelativeSizeSpan(1.2f), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            builder.append("\n")
            builder.append(prohibitContent)

            builder.toSpanned()
        }
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
