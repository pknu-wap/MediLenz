package com.android.mediproject.core.model.dur.durproduct.combination

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

data class DurProductCombinationTaboo(
    val itemName: String,
    val ingrKorName: String,
    val ingrEngName: String,
    val className: String,
    val mixtureClassName: String,
    val mixtureIngrKorName: String,
    val mixtureIngrEngName: String,
    val remark: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.COMBINATION_TABOO) {
    override val content: Spanned = WeakReference(SpannableStringBuilder()).get()!!.let { builder ->
        builder.append(itemName)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(RelativeSizeSpan(1.2f), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append("\n")
        builder.append(prohibitContent)

        builder.toSpanned()
    }
}

class DurProductCombinationTabooWrapper(
    override val response: DurProductCombinationTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductCombinationTaboo(
            itemName = it.itemName,
            ingrKorName = it.ingrKorName,
            ingrEngName = it.ingrEngName,
            className = it.className,
            mixtureClassName = it.mixtureClassName,
            mixtureIngrKorName = it.mixtureIngrKorName,
            mixtureIngrEngName = it.mixtureIngrEngName,
            remark = it.remark,
            prohibitContent = it.prohibitContent,
        )
    }

}
