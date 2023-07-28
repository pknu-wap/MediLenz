package com.android.mediproject.core.model.dur.durproduct.specialtyagegroup

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
 * 특정연령대 금기
 *
 * @param itemName 품목명
 * @param prohibitContent 연령금기내용
 */
data class DurProductSpecialtyAgeGroupTaboo(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.SPECIALTY_AGE_GROUP_TABOO) {
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

class DurProductSpecialtyAgeGroupTabooWrapper(
    override val response: DurProductSpecialtyAgeGroupTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductSpecialtyAgeGroupTaboo(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }

}
