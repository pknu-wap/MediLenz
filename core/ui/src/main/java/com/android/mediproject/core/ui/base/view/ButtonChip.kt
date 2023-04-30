package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.android.mediproject.core.ui.R
import com.google.android.material.chip.Chip

/**
 * Chip을 Button처럼 사용하기 위한 CustomView
 *
 * 예를 들어 최근 검색 목록에서 검색어를 보여주는 버튼 뷰입니다.
 *
 * 클릭 시 해당 내용을 가진 다음 화면으로 이동이 가능합니다.
 */
class ButtonChip<T> : Chip {

    constructor(context: Context) : super(context, null) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs) {
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


    private fun init(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonChip,
            com.google.android.material.R.attr.chipStyle,
            com.google.android.material.R.style.Widget_Material3_Chip_Assist_Elevated
        ).apply {
            recycle()
        }

        isClickable = true
        setTextColor(resources.getColor(R.color.button_chip_text_color, null))
        chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.button_chip_text_color, null))
    }

    fun setChipText(text: String) {
        this.text = text
    }


    fun setOnChipClickListener(listener: OnClickListener<T>) {
        setOnClickListener {
            listener.onClick(data)
        }
    }
}