package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.mediproject.core.ui.R

class Subtitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var title : TextView
    lateinit var redPoint : TextView
    lateinit var inputData : EditText

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val li = context.getSystemService(infService) as LayoutInflater
        val view = li.inflate(R.layout.subtitle, this, false)
        addView(view)

        title = findViewById<TextView>(R.id.subtitleTitleTV)
        redPoint = findViewById<TextView>(R.id.subtitlePointTV)
        inputData = findViewById<EditText>(R.id.subtitleEDT)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Subtitle)

        val titleText = typedArray.getString(R.styleable.Subtitle_title) ?: ""
        val point = typedArray.getBoolean(R.styleable.Subtitle_redPoint, true)
        val hintText = typedArray.getString(R.styleable.Subtitle_edtHint) ?: ""

        title.text = titleText
        inputData.hint = hintText
        redPoint.visibility = when(point){
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }

        typedArray.recycle()
    }

    //EditText에 적은 값을 불러오는 함수
    fun getValue() : String = inputData.text.toString()
}