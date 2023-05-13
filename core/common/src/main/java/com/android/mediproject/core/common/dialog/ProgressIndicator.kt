package com.android.mediproject.core.common.dialog

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.android.mediproject.core.common.R
import com.android.mediproject.core.common.uiutil.dpToPx

class ProgressIndicator(context: Context, attrs: AttributeSet?, textMessage: String?) : ConstraintLayout(context, attrs) {

    private val lottie = LottieAnimationView(context).apply {
        setAnimation(R.raw.bluedottedprogress)
        repeatCount = INFINITE
        id = R.id.progressBar
    }

    private val textView = TextView(context).apply {
        textMessage?.apply {
            text = this
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            typeface = Typeface.create("sans-serif-thin", Typeface.NORMAL)
        } ?: kotlin.run { visibility = GONE }

        gravity = Gravity.CENTER_HORIZONTAL
        id = R.id.progressText
    }

    init {
        background = ContextCompat.getDrawable(context, R.drawable.progress_background)

        addView(lottie, LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context, 30)).apply {
            topToTop = LayoutParams.PARENT_ID
        })
        addView(textView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            topToBottom = lottie.id
            topMargin = dpToPx(context, 16)
        })

        lottie.playAnimation()
    }
}