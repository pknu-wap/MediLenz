package com.android.mediproject.core.common.uiutil

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Singleton
class SystemBarStyler @Inject constructor(
) {
    enum class StatusBarColor {
        BLACK, WHITE
    }

    enum class NavigationBarColor {
        BLACK, WHITE
    }

    enum class SpacingType {
        MARGIN, PADDING
    }

    private lateinit var window: Window

    data class ChangeView(val view: View, val type: SpacingType)

    private var statusBarHeight by Delegates.notNull<Int>()
    private var navigationBarHeight by Delegates.notNull<Int>()

    val statusBarHeightPx: Int
        get() = statusBarHeight

    val navigationBarHeightPx: Int
        get() = navigationBarHeight

    fun init(context: Context, window: Window) {
        this.window = window
        context.resources.apply {
            statusBarHeight = getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
            navigationBarHeight = getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }

    fun setStyle(activity: Activity?, statusBarColor: StatusBarColor) {
        if (activity != null) {
            var newValue = 0
            newValue = if (statusBarColor == StatusBarColor.BLACK) {
                // 상단바 블랙으로
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 상단바 하양으로
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
            if (activity.window.decorView.systemUiVisibility != newValue) activity.window.decorView.systemUiVisibility = newValue
        }
    }

    fun setStyle(statusBarColor: StatusBarColor, navigationBarColor: NavigationBarColor) {
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = statusBarColor == StatusBarColor.BLACK
            isAppearanceLightNavigationBars = navigationBarColor == NavigationBarColor.BLACK
        }
    }


    fun changeMode(topViews: List<ChangeView>, bottomViews: List<ChangeView>) {
        topViews.forEach { topView ->
            if (topView.type == SpacingType.MARGIN) {
                topView.view.updateLayoutParams {
                    (this as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
                }
            } else topView.view.updatePadding(left = 0, right = 0, top = statusBarHeight, bottom = 0)
        }
        bottomViews.forEach { bottomView ->
            if (bottomView.type == SpacingType.MARGIN) {
                bottomView.view.updateLayoutParams {
                    (this as ViewGroup.MarginLayoutParams).bottomMargin = navigationBarHeight
                }
            } else {
                if (bottomView in topViews) {
                    bottomView.view.updatePadding(left = 0, right = 0, top = statusBarHeight, bottom = navigationBarHeight)
                } else {
                    bottomView.view.updatePadding(left = 0, right = 0, top = 0, bottom = navigationBarHeight)
                }
            }
        }
    }
}