package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.android.mediproject.core.ui.R

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
        context.theme.obtainStyledAttributes(attrs, R.styleable.MediSearchbar, 0, 0).apply {
            try {
                val cameraIcon = getDrawable(R.styleable.MediSearchbar_camera_icon)
                val title = getString(R.styleable.MediSearchbar_search_hint)
                val aiTitle = getString(R.styleable.MediSearchbar_ai_text)
                val searchIcon = getDrawable(R.styleable.MediSearchbar_search_icon)
                val aiFontSize = getDimension(R.styleable.MediSearchbar_ai_btn_font_size, 13f)
                val titleFontSize = getDimension(R.styleable.MediSearchbar_hint_font_size, 13f)

                searchEditView = EditText(context)
                searchAiBtnView = TextView(context)
                searchManualBtnView = ImageView(context)

                setBackgroundResource(R.drawable.search_bar)
                elevation = 4f
                clipChildren = false

                // title
                searchEditView.apply {
                    hint = title
                    textSize = titleFontSize
                }

                // ai btn
                searchAiBtnView.apply {
                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    val horizontalPadding = context.resources.getDimension(R.dimen.ai_button_horizontal_padding)
                    val verticalPadding = context.resources.getDimension(R.dimen.ai_button_vetical_padding)

                    setPadding(horizontalPadding.toInt(), verticalPadding.toInt(), horizontalPadding.toInt(), verticalPadding.toInt())
                    setCompoundDrawables(cameraIcon, null, null, null)
                    text = aiTitle
                    textSize = aiFontSize
                    setBackgroundResource(R.drawable.search_with_ai_button)
                }

                // search btn
                searchManualBtnView.apply {
                    setImageDrawable(searchIcon)
                    updateLayoutParams {
                        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics).toInt()
                        height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics).toInt()
                    }
                }

                addView(searchManualBtnView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                    rightToRight = LayoutParams.PARENT_ID
                })

                addView(searchAiBtnView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                    rightToLeft = searchManualBtnView.id
                })

                addView(searchEditView, LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                    rightToLeft = searchAiBtnView.id
                    leftToLeft = LayoutParams.PARENT_ID
                })

            } finally {
                recycle()
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
}
