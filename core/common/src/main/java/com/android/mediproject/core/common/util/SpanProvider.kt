package com.android.mediproject.core.common.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import com.android.mediproject.core.common.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpanProvider @Inject constructor() {

    companion object {
        private const val TEXT_SIZE_PERCENT = 1.2F
    }

    fun homeInitHeaderSpan(context: Context, text: String): SpannableStringBuilder {
        return SpannableStringBuilder(text).apply {
            val underline1Idx =
                text.indexOf(context.getString(R.string.highlightWord1)) to text.indexOf(context.getString(R.string.highlightWord1)) + 2

            setSpan(UnderlineSpan(), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(
                RelativeSizeSpan(TEXT_SIZE_PERCENT),
                underline1Idx.first,
                underline1Idx.second,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE,
            )

            val underline2Idx =
                text.indexOf(context.getString(R.string.highlightWord2)) to text.indexOf(context.getString(R.string.highlightWord2)) + 2

            setSpan(UnderlineSpan(), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(
                RelativeSizeSpan(TEXT_SIZE_PERCENT),
                underline2Idx.first,
                underline2Idx.second,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE,
            )
        }
    }

    fun penaltyGetNoHistorySpan(context: Context): SpannableStringBuilder {
        val text = context.getString(R.string.failedLoading)
        val highLightIndex = text.indexOf(context.getString(R.string.highlightWord3))
        return SpannableStringBuilder(text).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.main)),
                highLightIndex,
                highLightIndex + 3,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), highLightIndex, highLightIndex + 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    fun myPageSetNoShowCommentListSpan(context: Context): SpannableStringBuilder {
        return SpannableStringBuilder(context.getString(R.string.noMyComment)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.main,
                    ),
                ),
                7, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(
                UnderlineSpan(),
                7,
                9,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
        }
    }

    fun myPageSetGuestModeScreenSpan(context: Context): SpannableStringBuilder {
        return SpannableStringBuilder(context.getString(R.string.guestDescription)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.main,
                    ),
                ),
                15, 18,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(UnderlineSpan(), 15, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    fun myPageMoreDialogGetWithdrawalSpan(context : Context): SpannableStringBuilder {
        return SpannableStringBuilder(context.getString(R.string.withdrawalDescription)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.red,
                    ),
                ),
                4,
                8,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    fun myPageMoregGetLogoutSpan(context: Context): SpannableStringBuilder {
        return SpannableStringBuilder(context.getString(R.string.logoutDescription)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.red,
                    ),
                ),
                4,
                8,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
