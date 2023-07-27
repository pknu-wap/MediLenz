package com.android.mediproject.core.model.dur.durproduct.dosing

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
 * 투여기간 주의
 *
 * @param itemName 품목명
 * @param prohibitContent 최대 투여기간
 */
data class DurProductDosingCaution(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.DOSING_CAUTION) {
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


class DurProductDosingCautionWrapper(
    override val response: DurProductDosingCautionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductDosingCaution(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }
}
