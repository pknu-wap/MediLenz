package com.android.mediproject.core.ui.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.TextViewCompat
import com.android.mediproject.core.ui.R

@SuppressLint("ResourceType")
class MediSearchbar constructor(
    context: Context, attrs: AttributeSet
) : ConstraintLayout(context, attrs) {
    private val searchEditView: EditText
    private val searchManualBtnView: ImageView
    private val searchAiBtnView: TextView

    interface SearchQueryCallback {
        fun onSearchQuery(query: String)
        fun onEmptyQuery()
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.MediSearchbar, 0, 0).also { typedArr ->
            try {
                val cameraIcon = typedArr.getDrawable(R.styleable.MediSearchbar_camera_icon) ?: AppCompatResources.getDrawable(
                    context, R.drawable.baseline_camera_24
                )
                val searchHint = typedArr.getString(R.styleable.MediSearchbar_search_hint) ?: context.getString(R.string.search_hint)
                val aiTitle = typedArr.getString(R.styleable.MediSearchbar_ai_text) ?: context.getString(R.string.search_with_ai)
                val searchIcon = typedArr.getDrawable(R.styleable.MediSearchbar_search_icon) ?: AppCompatResources.getDrawable(
                    context, R.drawable.baseline_search_24
                )
                val aiFontSize = typedArr.getDimension(R.styleable.MediSearchbar_ai_btn_font_size, 12f)
                val titleFontSize = typedArr.getDimension(R.styleable.MediSearchbar_hint_font_size, 13f)

                val verticalSpace = context.resources.getDimension(R.dimen.search_bar_vertical_space).toInt()
                val horizontalSpace = context.resources.getDimension(R.dimen.search_bar_horizontal_space).toInt()

                // search btn
                searchManualBtnView = ImageView(context).apply {
                    setImageDrawable(searchIcon)
                    isClickable = true
                    
                    // 투명 배경에 ripple 효과를 주기 위함
                    val outValue = TypedValue()
                    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                    setBackgroundResource(outValue.resourceId)

                    layoutParams =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics).toInt().let { size ->
                            ConstraintLayout.LayoutParams(size, size).apply {
                                topToTop = LayoutParams.PARENT_ID
                                bottomToBottom = LayoutParams.PARENT_ID
                                rightToRight = LayoutParams.PARENT_ID
                                rightMargin = horizontalSpace
                            }
                        }
                    id = 30
                }

                // ai btn
                searchAiBtnView = TextView(context).apply {
                    layoutParams =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics).toInt().let { height ->
                            ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height).apply {
                                topToTop = LayoutParams.PARENT_ID
                                bottomToBottom = LayoutParams.PARENT_ID
                                rightToLeft = searchManualBtnView.id
                                rightMargin = horizontalSpace
                            }
                        }

                    isClickable = true
                    text = aiTitle
                    setTextColor(Color.WHITE)
                    textSize = aiFontSize

                    gravity = android.view.Gravity.CENTER_VERTICAL

                    val horizontalPadding = context.resources.getDimension(R.dimen.ai_button_horizontal_padding)
                    val verticalPadding = context.resources.getDimension(R.dimen.ai_button_vetical_padding)
                    setPadding(horizontalPadding.toInt(), verticalPadding.toInt(), horizontalPadding.toInt(), verticalPadding.toInt())

                    // set left drawable
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f, context.resources.displayMetrics).apply {
                        cameraIcon?.setBounds(0, 0, this.toInt(), this.toInt())
                    }

                    TextViewCompat.setCompoundDrawableTintList(this, ColorStateList.valueOf(Color.WHITE))
                    compoundDrawablePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()
                    setCompoundDrawables(cameraIcon, null, null, null)

                    // set
                    setBackgroundResource(R.drawable.search_with_ai_button)
                    id = 20
                }


                // edittext
                searchEditView = EditText(context).apply {
                    layoutParams = ConstraintLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                        topToTop = LayoutParams.PARENT_ID
                        bottomToBottom = LayoutParams.PARENT_ID
                        rightToLeft = searchAiBtnView.id
                        leftToLeft = LayoutParams.PARENT_ID
                        setMargins(horizontalSpace, verticalSpace, 0, verticalSpace)
                    }
                    hint = searchHint
                    textSize = titleFontSize
                    id = 10

                    maxLines = 1
                    background = null
                }

                this.apply {
                    setBackgroundResource(R.drawable.search_bar)

                    clipChildren = false
                    clipToPadding = false

                    addView(searchManualBtnView)
                    addView(searchAiBtnView)
                    addView(searchEditView)
                }

            } finally {
                typedArr.recycle()
            }

        }


    }

    /**
     * 버튼 클릭 시 검색 쿼리값을 반환한다.
     *
     * 검색 값이 없으면 onEmptyQuery() 호출
     */
    fun setOnSearchBtnClickListener(searchQueryCallback: SearchQueryCallback) {
        searchManualBtnView.setOnClickListener {
            searchEditView.text.toString().let { query ->
                if (query.isNotEmpty()) {
                    searchQueryCallback.onSearchQuery(query)
                } else {
                    searchQueryCallback.onEmptyQuery()
                }
            }
        }
    }

    /**
     * 버튼 클릭 시 다음 화면으로 넘어가도록 요청한다.
     */
    fun setOnSearchAiBtnClickListener(listener: OnClickListener) {
        searchAiBtnView.setOnClickListener(listener)
    }
}