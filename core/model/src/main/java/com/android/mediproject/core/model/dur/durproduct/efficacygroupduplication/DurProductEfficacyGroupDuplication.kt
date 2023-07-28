package com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication

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

data class DurProductEfficacyGroupDuplication(
    val itemName: String,
    val effectName: String,
    val className: String,
    val ingrKorName: String,
    val ingrEngName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.EFFICACY_GROUP_DUPLICATION) {
    override val content: Spanned
        get() = WeakReference(SpannableStringBuilder()).get()!!.let { builder ->
            builder.append(ingrEngName)
            builder.setSpan(StyleSpan(Typeface.BOLD), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.setSpan(RelativeSizeSpan(1.2f), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            builder.append("\n")
            builder.append(itemName)

            builder.toSpanned()
        }
}

class DurProductEfficacyGroupDuplicationWrapper(
    override val response: DurProductEfficacyGroupDuplicationResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductEfficacyGroupDuplication(
            itemName = it.itemName,
            effectName = it.effectName,
            className = it.className,
            ingrKorName = it.ingrName,
            ingrEngName = it.ingrEngName,
            prohibitContent = it.prohibitContent,
        )
    }
}
