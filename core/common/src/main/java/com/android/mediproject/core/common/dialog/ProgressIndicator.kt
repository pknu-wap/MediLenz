package com.android.mediproject.core.common.dialog

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
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
        text = textMessage
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        id = R.id.progressText
    }

    init {
        setPadding(dpToPx(context, 24))

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