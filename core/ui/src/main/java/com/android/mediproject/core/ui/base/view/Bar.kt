package com.android.mediproject.core.ui.base.view
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.mediproject.core.ui.R

class Bar @JvmOverloads constructor(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs){

    companion object{
        const val BLUE = 0
        const val WHITE = 1
    }
    lateinit var backButton : ImageView
    lateinit var title : TextView
    lateinit var bar : ConstraintLayout

    init{
        val inflaterService = Context.LAYOUT_INFLATER_SERVICE as LayoutInflater
        inflaterService.inflate(R.layout.bar,this,false)

        backButton = findViewById<ImageView>(R.id.barBackIV)
        title = findViewById<TextView>(R.id.barTitleTV)
        bar = findViewById<ConstraintLayout>(R.id.bar)

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.Bar)

        val backButtonVisible = typedArray.getBoolean(R.styleable.Bar_showBackButton, false)
        val titleText = typedArray.getString(R.styleable.Bar_title) ?: ""
        val barTheme = typedArray.getInt(R.styleable.Bar_theme,BLUE)

        backButton.visibility = when(backButtonVisible){
            true -> View.VISIBLE
            else -> View.GONE
        }

        title.text = titleText

        when(barTheme){
            BLUE -> {
                bar.setBackgroundColor(ContextCompat.getColor(context,R.color.main))
                title.setTextColor(ContextCompat.getColor(context,R.color.white))
                backButton.setImageResource(R.drawable.left_arrow)
            }
            WHITE -> {
                bar.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                title.setTextColor(ContextCompat.getColor(context,R.color.black))
                backButton.setImageResource(R.drawable.left_arrow_black)
            }
        }
    }

}