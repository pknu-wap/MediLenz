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
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf


private const val DOT_CHAR = "• "

/**
 * • 추천 검색어  와 같이 아래에 목록으로된 데이터가 나올때 헤더로 사용될 뷰
 *
 * 헤더 텍스트, 확장 버튼, 더보기 버튼
 */
@SuppressLint("ResourceType")
class HeaderForElementsView constructor(
    context: Context, attrs: AttributeSet,
) : ConstraintLayout(context, attrs), OnIndicatorVisibilityChangedListener {
    private val expandBtnView: ImageView
    private val moreBtnView: TextView
    private val titleView: TextView
    private var expanded = true
        set(value) {
            field = value
            expandBtnView.setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    if (value) R.drawable.baseline_expand_more_24 else R.drawable.baseline_expand_less_24,
                ),
            )

            takeIf { targetViewId != -1 }?.let {
                (parent.parent as View).findViewById<View>(targetViewId)?.apply {
                    visibility = if (value) View.VISIBLE
                    else View.GONE
                }
            }

            onExpandClickListener?.onExpandClick(value)
        }


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
                moreVisibility = typedArr.getInt(R.styleable.HeaderForElementsView_more_visibility, moreVisibility)
                targetViewId = typedArr.getResourceId(R.styleable.HeaderForElementsView_visibility_target_view, -1)

                // title
                titleView = TextView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        topToTop = LayoutParams.PARENT_ID
                        bottomToBottom = LayoutParams.PARENT_ID
                        leftToLeft = LayoutParams.PARENT_ID
                        rightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()
                    }
                    textSize = titleFontSize
                    setTextColor(titleColor)
                    id = 1

                    setOnClickListener {
                        this@HeaderForElementsView.expanded = !this@HeaderForElementsView.expanded
                    }
                }
                setTitle(title)


                // 확장 버튼
                expandBtnView = ImageView(context).apply {
                    setOnClickListener {
                        this@HeaderForElementsView.expanded = !this@HeaderForElementsView.expanded
                    }

                    isClickable = true

                    layoutParams =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt().let { size ->
                            LayoutParams(size, size).apply {
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
                        LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
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
                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
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

    /**
     * 더보기 버튼 Visible 관련 로직
     */
    fun setMoreVisiblity(boolean: Boolean) {
        when (boolean) {
            false -> moreBtnView.visibility = View.GONE
            true -> moreBtnView.visibility = View.VISIBLE
        }
    }

    /**
     * 확장 버튼 Visible 관련 로직
     */
    fun setExpandVisiblity(boolean: Boolean) {
        when (boolean) {
            false -> expandBtnView.visibility = View.GONE
            true -> expandBtnView.visibility = View.VISIBLE
        }
    }


    /**
     * 인디케이터의 Visible 여부에 따라 확장 버튼의 Visible 여부를 결정
     *
     * @param visibility 인디케이터의 Visible 여부
     */
    override fun onIndicatorVisibilityChanged(visibility: Boolean) {
        findViewById<CircularProgressIndicator>(R.id.indicator)?.apply {
            isVisible = visibility
            expandBtnView.isVisible = !visibility
            expanded = !visibility
            if (visibility) show()
            else hide()
        }
    }
}

fun interface OnIndicatorVisibilityChangedListener {
    fun onIndicatorVisibilityChanged(visibility: Boolean)
}


suspend inline fun <reified T> Flow<UiState<T>>.stateAsCollect(
    headerForElementsView: HeaderForElementsView, noDataWarningView: View?,
): Flow<UiState<T>> = flatMapLatest {
    when (it) {
        is UiState.Error -> {
            headerForElementsView.onIndicatorVisibilityChanged(false)
            noDataWarningView?.isVisible = true
        }

        is UiState.Loading -> {
            headerForElementsView.onIndicatorVisibilityChanged(true)
            noDataWarningView?.isVisible = false
        }

        is UiState.Success -> {
            headerForElementsView.onIndicatorVisibilityChanged(false)
            noDataWarningView?.isVisible = false
        }

        is UiState.Initial -> {
            headerForElementsView.onIndicatorVisibilityChanged(true)
            noDataWarningView?.isVisible = false
        }
    }
    flowOf(it)
}

object BindingAdpater {
    @JvmStatic
    @BindingAdapter("onMoreClick")
    fun setOnMoreClick(view: HeaderForElementsView, onClickListenr: View.OnClickListener) {
        view.findViewById<TextView>(30).setOnClickListener(onClickListenr)
    }
}
