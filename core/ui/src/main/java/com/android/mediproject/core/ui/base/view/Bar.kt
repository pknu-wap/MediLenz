package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.android.mediproject.core.ui.R

class Bar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        const val BLUE = 0
        const val WHITE = 1
        const val HOME = 2
    }

    lateinit var backButton: ImageView
    lateinit var title: TextView
    lateinit var bar: ConstraintLayout
    lateinit var logo : ImageView

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val li = context.getSystemService(infService) as LayoutInflater
        val view = li.inflate(R.layout.bar, this, false)
        addView(view)


        backButton = findViewById<ImageView>(R.id.barBackIV)
        title = findViewById<TextView>(R.id.barTitleTV)
        bar = findViewById<ConstraintLayout>(R.id.bar)
        logo = findViewById<ImageView>(R.id.barLogoIV)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bar)

        val backButtonVisible = typedArray.getBoolean(R.styleable.Bar_showBackButton, false)
        val titleText = typedArray.getString(R.styleable.Bar_title) ?: ""
        val barTheme = typedArray.getInt(R.styleable.Bar_setTheme, BLUE)

        when (backButtonVisible) {
            true -> backButton.visibility = View.VISIBLE
            else -> backButton.visibility = View.GONE
        }

        backButton.setOnClickListener { findNavController().popBackStack() }

        title.text = titleText

        when (barTheme) {
            BLUE -> {
                bar.setBackgroundColor(ContextCompat.getColor(context, R.color.main))
                title.setTextColor(ContextCompat.getColor(context, R.color.white))
                backButton.setImageResource(R.drawable.left_arrow)
            }

            WHITE -> {
                bar.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                title.setTextColor(ContextCompat.getColor(context, R.color.black))
                backButton.setImageResource(R.drawable.left_arrow_black)
            }

            HOME -> {
                bar.setBackgroundColor(ContextCompat.getColor(context, R.color.main))
                backButton.visibility = View.GONE
                title.visibility = View.GONE
                logo.visibility = View.VISIBLE
            }
        }

        typedArray.recycle()
    }

}