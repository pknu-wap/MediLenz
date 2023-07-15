package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.mediproject.core.ui.R

class EtcBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var title: TextView
    lateinit var arrow: ImageView

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val li = context.getSystemService(infService) as LayoutInflater
        val view = li.inflate(R.layout.etcbar, this, false)
        addView(view)

        title = findViewById<TextView>(R.id.etcBarTV)
        arrow = findViewById<ImageView>(R.id.etcBarIV)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EtcBar)
        val arrowVisible = typedArray.getBoolean(R.styleable.EtcBar_showArrow, true)

        title.text = typedArray.getString(R.styleable.EtcBar_title) ?: ""
        arrow.visibility = when (arrowVisible) {
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }

        typedArray.recycle()
    }

}