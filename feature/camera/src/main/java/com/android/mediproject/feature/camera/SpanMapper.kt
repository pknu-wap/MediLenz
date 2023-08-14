package com.android.mediproject.feature.camera

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat

object SpanMapper {
    private val spanCache = mutableMapOf<String, Spannable>()

    fun createCheckCountsOfMedicinesMessage(context: Context): Spannable {
        return spanCache["checkCountsOfMedicinesMessage"] ?: run {
            val span = SpannableStringBuilder()
            val searchKeyword = context.getString(R.string.search)
            val message = context.getString(R.string.checkCountsOfMedicinesMessage)
            val (start, end) = message.indexOf(searchKeyword).run { this to this + searchKeyword.length }
            span.append(message)
            span.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
            )
            span.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, com.android.mediproject.core.ui.R.color.main)),
                start,
                end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
            )

            spanCache["checkCountsOfMedicinesMessage"] = span
            span
        }
    }

    fun createCheckCountsOfMedicinesTitle(context: Context, count: Int): Spannable {
        return spanCache["checkCountsOfMedicinesTitle"] ?: run {
            val span = SpannableStringBuilder()
            val countStr = count.toString()
            val message = context.getString(R.string.checkCountsOfMedicines)
            span.append("$countStr$message")
            span.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                countStr.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
            )
            span.setSpan(
                RelativeSizeSpan(1.3f),
                0,
                countStr.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
            )

            spanCache["checkCountsOfMedicinesTitle"] = span
            span
        }
    }
}
