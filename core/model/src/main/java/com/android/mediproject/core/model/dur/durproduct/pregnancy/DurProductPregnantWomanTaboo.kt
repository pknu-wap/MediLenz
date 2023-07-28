package com.android.mediproject.core.model.dur.durproduct.pregnancy

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
 * 임부 금기
 *
 * @param itemName 품목명
 * @param ingrKorName 성분명(한글)
 * @param ingrEngName 성분명(영문)
 * @param prohibitContent 금기 내용
 */
data class DurProductPregnantWomanTaboo(
    val itemName: String,
    val ingrKorName: String,
    val ingrEngName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.PREGNANT_WOMAN_TABOO) {
    override val content: Spanned
        get() = WeakReference(SpannableStringBuilder()).get()!!.let { builder ->
            builder.append(itemName)
            builder.setSpan(StyleSpan(Typeface.BOLD), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.setSpan(RelativeSizeSpan(1.2f), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            builder.append(ingrEngName)
            builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - ingrEngName.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            builder.append("\n")
            builder.append(prohibitContent)

            builder.toSpanned()
        }
}

class DurProductPregnantWomanTabooWrapper(
    override val response: DurProductPregnantWomanTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductPregnantWomanTaboo(
            itemName = it.itemName,
            ingrKorName = it.ingrName,
            ingrEngName = it.ingrEngName,
            prohibitContent = it.prohibitContent,
        )
    }

}
