package com.android.mediproject.core.ui.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.mediproject.core.ui.R

/**
 * 최근 댓글 목록에서 댓글 아이템을 보여주는 역할을 할 수 있는 공통적으로 사용할 아이템 뷰입니다.
 *
 * 클릭 시 해당 내용을 가진 다음 화면으로 이동이 가능합니다.
 */
class SimpleListItemView<T> : ConstraintLayout {

    constructor(context: Context, midRatio: Float) : super(context, null) {
        init(context, null, midRatio)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs) {
        init(context, attrs)
    }

    fun interface OnClickListener<T> {
        fun onClick(e: T?)
    }

    private var _data: T? = null
    var data: T?
        set(value) {
            _data = value
        }
        get() = _data

    @SuppressLint("ResourceType")
    private val chip = ButtonChip<T>(context).apply {
        textSize = 13f
        isClickable = false
        id = 10
    }

    @SuppressLint("ResourceType")
    private val textView = TextView(context).apply {
        ellipsize = TextUtils.TruncateAt.END
        isClickable = false
        maxLines = 1
        textAlignment = TEXT_ALIGNMENT_VIEW_START
        textSize = 12f
        id = 20
    }

    private fun init(context: Context, attrs: AttributeSet?, midRatio: Float = 0.6f) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SimpleCommentItemView, 0, 0
        ).apply {
            try {
                // 투명 배경에 ripple 효과를 주기 위함
                val outValue = TypedValue()
                context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackground, outValue, true
                )
                setBackgroundResource(outValue.resourceId)
                // 수평 마진
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
                    .toInt().apply { setPadding(0, this, 0, this) }

                addView(chip, LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                    leftToLeft = LayoutParams.PARENT_ID
                    rightToLeft = textView.id
                    horizontalBias = midRatio
                    horizontalChainStyle = LayoutParams.CHAIN_PACKED
                })
                addView(textView, LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                    leftToRight = chip.id
                    rightToRight = LayoutParams.PARENT_ID
                    leftMargin = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics
                    ).toInt()
                })

                isClickable = true
                clipToPadding = false
                clipChildren = false

                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            } finally {
                recycle()

            }
        }
    }

    fun setTitle(text: String): SimpleListItemView<T> {
        chip.setChipText(text)
        return this
    }

    fun setContent(text: String): SimpleListItemView<T> {
        textView.text = text
        return this
    }

    fun setTitleColor(@ColorInt color: Int): SimpleListItemView<T> {
        chip.setTextColor(color)
        return this
    }

    fun setChipStrokeColor(@ColorRes color: Int): SimpleListItemView<T> {
        chip.setChipStrokeColorResource(color)
        return this
    }

    fun setContentTextColor(@ColorInt color: Int): SimpleListItemView<T> {
        textView.setTextColor(color)
        return this
    }


    fun setOnItemClickListener(listener: OnClickListener<T>) {
        setOnClickListener {
            listener.onClick(data)
        }
    }
}