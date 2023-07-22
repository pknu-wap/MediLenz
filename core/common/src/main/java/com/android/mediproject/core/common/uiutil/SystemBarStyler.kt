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
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import javax.inject.Inject
import kotlin.properties.Delegates

class SystemBarStyler @Inject constructor(
) : LayoutController, SystemBarController {
    enum class SystemBarColor {
        BLACK, WHITE
    }

    enum class SpacingType {
        MARGIN, PADDING
    }

    private var acitivityLayoutController: LayoutController? = null
    private var windowInsetsController: WindowInsetsControllerCompat? = null

    private var window: Window by Delegates.notNull()

    data class ChangeView(val view: View, val type: SpacingType)

    private var statusBarHeight by Delegates.notNull<Int>()
    private var navigationBarHeight by Delegates.notNull<Int>()

    override val statusBarHeightPx: Int
        get() = statusBarHeight

    override val navigationBarHeightPx: Int
        get() = navigationBarHeight

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    override fun init(context: Context, window: Window, activityLayoutController: LayoutController) {
        this.window = window
        this.acitivityLayoutController = activityLayoutController

        Resources.getSystem().run {
            statusBarHeight = getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
            navigationBarHeight = getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.apply {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            navigationBarColor = Color.TRANSPARENT
            statusBarColor = Color.TRANSPARENT
        }
        windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    }


    override fun setStyle(statusBarColor: SystemBarColor, navBarColor: SystemBarColor) {
        windowInsetsController?.apply {
            isAppearanceLightStatusBars = statusBarColor == SystemBarColor.BLACK
            isAppearanceLightNavigationBars = navBarColor == SystemBarColor.BLACK
        }
    }


    override fun changeMode(topViews: List<ChangeView>, bottomViews: List<ChangeView>) {
        topViews.forEach { topView ->
            if (topView.type == SpacingType.MARGIN) {
                topView.view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin += statusBarHeight
                }
            } else {
                topView.view.updatePadding(top = topView.view.paddingTop + statusBarHeight)
            }
        }
        bottomViews.forEach { bottomView ->
            if (bottomView.type == SpacingType.MARGIN) {
                bottomView.view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin += navigationBarHeight
                }
            } else {
                bottomView.view.updatePadding(
                    bottom = bottomView.view.paddingBottom + navigationBarHeight,
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
    fun setStyle(statusBarColor: SystemBarStyler.SystemBarColor, navBarColor: SystemBarStyler.SystemBarColor)

    fun changeMode(topViews: List<SystemBarStyler.ChangeView> = emptyList(), bottomViews: List<SystemBarStyler.ChangeView> = emptyList())
}
