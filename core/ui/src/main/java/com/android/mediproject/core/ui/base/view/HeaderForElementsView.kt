package com.android.mediproject.core.ui.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.mediproject.core.ui.R
import com.google.android.material.progressindicator.CircularProgressIndicator


private const val DOT_CHAR = "• "

/**
 * • 추천 검색어  와 같이 아래에 목록으로된 데이터가 나올때 헤더로 사용될 뷰
 *
 * 헤더 텍스트, 확장 버튼, 더보기 버튼
 */
@SuppressLint("ResourceType")
class HeaderForElementsView constructor(
    context: Context, attrs: AttributeSet
) : ConstraintLayout(context, attrs), OnIndicatorVisibilityChangedListener {
    private val expandBtnView: ImageView
    private val moreBtnView: TextView
    private val titleView: TextView
    private var expanded = true
    private var moreVisibility = View.VISIBLE

    private var onExpandClickListener: OnExpandClickListener? = null

    private val targetViewId: Int


    fun interface OnExpandClickListener {
        /**
         * 확장 버튼 클릭 시 호출
         *
         * @param isExpanded 확장 여부
         *
         * 확장 버튼을 클릭 후 확장상태가 되면 return true, 축소상태가 되면 return false
         */
        fun onExpandClick(isExpanded: Boolean)
    }

    fun interface OnMoreClickListener {
        /**
         * 더보기 버튼 클릭 시 호출
         */
        fun onMoreClick()
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.HeaderForElementsView, 0, 0).also { typedArr ->
            try {
                val title = typedArr.getString(R.styleable.HeaderForElementsView_header_title) ?: ""
                val titleColor = typedArr.getColor(R.styleable.HeaderForElementsView_header_title_color, Color.BLACK)
                val moreTitle = typedArr.getString(R.styleable.HeaderForElementsView_more_title)
                val moreColor = typedArr.getColor(R.styleable.HeaderForElementsView_more_color, R.color.gray2)
                val iconColor = typedArr.getColor(R.styleable.HeaderForElementsView_expand_icon_color, Color.BLACK)
                val titleFontSize = typedArr.getDimension(R.styleable.HeaderForElementsView_title_text_size, 15f)
                val moreFontSize = typedArr.getDimension(R.styleable.HeaderForElementsView_more_text_size, 14f)
                expanded = typedArr.getBoolean(R.styleable.HeaderForElementsView_is_expanded, expanded)
                moreVisibility = typedArr.getInt(R.styleable.HeaderForElementsView_more_visibility, moreVisibility)
                targetViewId = typedArr.getResourceId(R.styleable.HeaderForElementsView_visibility_target_view, -1)


                // title
                titleView = TextView(context).apply {
                    layoutParams = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        topToTop = LayoutParams.PARENT_ID
                        bottomToBottom = LayoutParams.PARENT_ID
                        leftToLeft = LayoutParams.PARENT_ID
                        rightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()
                    }
                    textSize = titleFontSize
                    setTextColor(titleColor)
                    id = 1

                    setOnClickListener {
                        setExpand(!expanded)
                    }
                }
                setTitle(title)


                // 확장 버튼
                expandBtnView = ImageView(context).apply {
                    setOnClickListener {
                        setExpand(!expanded)
                    }

                    isClickable = true

                    layoutParams =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt().let { size ->
                            ConstraintLayout.LayoutParams(size, size).apply {
                                topToTop = LayoutParams.PARENT_ID
                                bottomToBottom = LayoutParams.PARENT_ID
                                leftToRight = titleView.id
                            }
                        }
                    id = 20
                    imageTintList = ColorStateList.valueOf(iconColor)

                    // 투명 배경에 ripple 효과를 주기 위함
                    val outValue = TypedValue()
                    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                    setBackgroundResource(outValue.resourceId)
                }


                CircularProgressIndicator(context).apply {
                    layoutParams =
                        ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                            topToTop = LayoutParams.PARENT_ID
                            bottomToBottom = LayoutParams.PARENT_ID
                            leftToRight = expandBtnView.id
                        }
                    id = R.id.indicator

                    isIndeterminate = true
                    trackThickness = 5

                    indicatorSize = titleFontSize.toInt() * 3

                    this@HeaderForElementsView.addView(this)
                }

                // 더 보기 버튼
                moreBtnView = TextView(context).apply {
                    layoutParams = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        topToTop = LayoutParams.PARENT_ID
                        bottomToBottom = LayoutParams.PARENT_ID
                        rightToRight = LayoutParams.PARENT_ID
                    }
                    visibility = moreVisibility
                    isClickable = true

                    text = moreTitle
                    textSize = moreFontSize
                    setTextColor(moreColor)
                    id = 30

                    // 투명 배경에 ripple 효과를 주기 위함
                    val outValue = TypedValue()
                    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                    setBackgroundResource(outValue.resourceId)
                }

                apply {
                    addView(titleView)
                    addView(expandBtnView)
                    addView(moreBtnView)

                    clipChildren = false
                }

            } finally {
                typedArr.recycle()
            }

        }

    }

    fun setTitle(title: String) {
        "$DOT_CHAR  $title".apply {
            titleView.text = this
        }
    }


    /**
     * 확장 버튼 클릭 콜백 설정
     */
    fun setOnExpandClickListener(onExpandClickListener: OnExpandClickListener) {
        this.onExpandClickListener = onExpandClickListener
    }

    /**
     * 더보기 버튼 클릭 콜백 설정
     */
    fun setOnMoreClickListener(onMoreClickListener: OnMoreClickListener) {
        moreBtnView.setOnClickListener {
            onMoreClickListener.onMoreClick()
        }
    }

    fun setMoreVisiblity(boolean: Boolean){
        when(boolean){
            false -> moreBtnView.visibility = View.GONE
            true -> moreBtnView.visibility = View.VISIBLE
        }
    }


    /**
     * 확장 버튼 클릭 시 호출
     *
     * @param expanded 확장 여부
     *
     */
    fun setExpand(expanded: Boolean) {
        this.expanded = expanded
        expandBtnView.setImageDrawable(
            AppCompatResources.getDrawable(
                context, if (expanded) R.drawable.baseline_expand_more_24 else R.drawable.baseline_expand_less_24
            )
        )

        takeIf { targetViewId != -1 }?.let {
            (parent.parent as View).findViewById<View>(targetViewId)?.apply {
                visibility = if (expanded) View.VISIBLE
                else View.GONE
            }
        }

        onExpandClickListener?.onExpandClick(expanded)
    }

    override fun onIndicatorVisibilityChanged(visibility: Boolean) {
        findViewById<CircularProgressIndicator>(R.id.indicator)?.apply {

            val visible = if (visibility) View.VISIBLE else View.GONE
            setVisibility(visible)
            when (visibility) {
                true -> {
                    show()
                    expandBtnView.visibility = View.GONE
                }

                false -> {
                    hide()
                    expandBtnView.visibility = View.VISIBLE
                }
            }
        }
    }

}

fun interface OnIndicatorVisibilityChangedListener {
    fun onIndicatorVisibilityChanged(visibility: Boolean)
}