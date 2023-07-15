package com.android.mediproject.core.common.uiutil

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import javax.inject.Inject
import kotlin.properties.Delegates

@SuppressLint("InternalInsetResource", "DiscouragedApi", "Deprecation")
class SystemBarStyler @Inject constructor(
) : LayoutController, SystemBarController {
    enum class StatusBarColor {
        BLACK, WHITE
    }

    enum class NavigationBarColor {
        BLACK, WHITE
    }

    enum class SpacingType {
        MARGIN, PADDING
    }

    private var acitivityLayoutController: LayoutController? = null

    private var window: Window by Delegates.notNull()

    data class ChangeView(val view: View, val type: SpacingType)

    private var statusBarHeight by Delegates.notNull<Int>()
    private var navigationBarHeight by Delegates.notNull<Int>()

    override val statusBarHeightPx: Int
        get() = statusBarHeight

    override val navigationBarHeightPx: Int
        get() = navigationBarHeight

    override fun init(context: Context, window: Window, activityLayoutController: LayoutController) {
        this.window = window
        this.acitivityLayoutController = activityLayoutController

        Resources.getSystem().run {
            statusBarHeight = getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
            navigationBarHeight = getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
        }

        window.apply {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            navigationBarColor = Color.TRANSPARENT
            statusBarColor = Color.TRANSPARENT
        }

    }


    override fun setStyle(statusBarColor: StatusBarColor, navigationBarColor: NavigationBarColor) {
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = statusBarColor == StatusBarColor.BLACK
            isAppearanceLightNavigationBars = navigationBarColor == NavigationBarColor.BLACK
        }
    }

    override fun defaultStyle() {
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = true
        }
    }


    override fun changeMode(topViews: List<ChangeView>, bottomViews: List<ChangeView>) {
        topViews.forEach { topView ->
            if (topView.type == SpacingType.MARGIN) {
                topView.view.updateLayoutParams {
                    (this as ViewGroup.MarginLayoutParams).topMargin += statusBarHeight
                }
            } else topView.view.updatePadding(left = 0, right = 0, top = statusBarHeight, bottom = 0)
        }
        bottomViews.forEach { bottomView ->
            if (bottomView.type == SpacingType.MARGIN) {
                bottomView.view.updateLayoutParams {
                    (this as ViewGroup.MarginLayoutParams).bottomMargin += navigationBarHeight
                }
            } else {
                bottomView.view.updatePadding(
                    left = 0, right = 0, top = if (bottomView in topViews) statusBarHeight else 0,
                    bottom = navigationBarHeight,
                )
            }
        }
    }

    override fun changeFragmentContainerHeight(isFull: Boolean) {
        acitivityLayoutController?.changeFragmentContainerHeight(isFull)
    }
}

fun interface LayoutController {
    fun changeFragmentContainerHeight(isFull: Boolean)
}

interface SystemBarController {

    val statusBarHeightPx: Int

    val navigationBarHeightPx: Int

    fun init(context: Context, window: Window, activityLayoutController: LayoutController)
    fun setStyle(statusBarColor: SystemBarStyler.StatusBarColor, navigationBarColor: SystemBarStyler.NavigationBarColor)
    fun defaultStyle()

    fun changeMode(topViews: List<SystemBarStyler.ChangeView> = emptyList(), bottomViews: List<SystemBarStyler.ChangeView> = emptyList())
}
