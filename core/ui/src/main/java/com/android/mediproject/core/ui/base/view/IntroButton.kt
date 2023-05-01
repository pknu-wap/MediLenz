package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import com.android.mediproject.core.ui.R

class IntroButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var buttonImageView: ImageView
    lateinit var title: TextView
    lateinit var introButton: FrameLayout

    companion object {
        const val NORMAL = 0
        const val WHITE = 1
    }

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val li = context.getSystemService(infService) as LayoutInflater
        val view = li.inflate(R.layout.introbutton, this, false)
        addView(view)

        title = findViewById<TextView>(R.id.introButtonTV)
        buttonImageView = findViewById<ImageView>(R.id.introButtonIV)
        introButton = findViewById<FrameLayout>(R.id.introButton)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IntroButton)

        val titleText = typedArray.getString(R.styleable.IntroButton_title) ?: ""
        val titleColor = typedArray.getColor(R.styleable.IntroButton_setTitleColor, 0)
        val buttonImage = typedArray.getResourceId(R.styleable.IntroButton_button_icon, -1)
        val setStroke = typedArray.getInt(R.styleable.IntroButton_setStroke, NORMAL)

        title.text = titleText
        title.setTextColor(titleColor)

        if (buttonImage != -1) buttonImageView.setImageResource(buttonImage)
        else buttonImageView.visibility = View.INVISIBLE

        if(setStroke == WHITE){
            Log.d("tgyuu","white")
            introButton.setBackgroundResource(R.drawable.rectangle_5_white)
        } else{
            Log.d("tgyuu","normal")
            introButton.setBackgroundResource(R.drawable.rectangle_5)
        }

        introButton.setBackgroundResource(
            when (setStroke) {
                WHITE -> R.drawable.rectangle_5_white
                NORMAL -> R.drawable.rectangle_5
                else -> R.drawable.rectangle_5
            }
        )
        typedArray.recycle()
    }
}