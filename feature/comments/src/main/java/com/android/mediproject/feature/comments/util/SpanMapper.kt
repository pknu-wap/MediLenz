package com.android.mediproject.feature.comments.util

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.android.mediproject.core.model.comments.BaseComment

internal object SpanMapper {

    private const val DOT = " • "

    fun createReplyToBaseCommentInfo(context: Context, baseComment: BaseComment): Spannable {
        val span = SpannableStringBuilder()
        val nameWithDot = baseComment.userName + DOT
        span.append(nameWithDot)
        span.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            nameWithDot.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
        )
        span.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            nameWithDot.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE,
        )

        span.append(baseComment.createdAt)
        return span
    }

}
