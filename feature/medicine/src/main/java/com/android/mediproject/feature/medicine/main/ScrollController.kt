package com.android.mediproject.feature.medicine.main

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.properties.Delegates

class ScrollController {

    private var viewBehavior: AppBarLayout.Behavior by Delegates.notNull()
    private var _targetOffset: Int = 0
    private val targetOffset: Int
        get() = _targetOffset

    private var removeOnOffsetChangedListenerFunc: (AppBarLayout.OnOffsetChangedListener) -> Unit by Delegates.notNull()
    private var addOnOffsetChangedListenerFunc: (AppBarLayout.OnOffsetChangedListener) -> Unit by Delegates.notNull()

    var scrollable: Boolean = true
        set(value) {
            if (value) {
                field = true
                viewBehavior.isVerticalOffsetEnabled = true
            } else {
                onDisable()
            }
        }

    private fun onDisable() {
        addOnOffsetChangedListenerFunc(
            object : AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (verticalOffset == targetOffset) {
                        removeOnOffsetChangedListenerFunc(this)
                        viewBehavior.isVerticalOffsetEnabled = false
                    }
                }
            },
        )
    }

    fun init(collapsingToolbarLayout: View, toolbar: View, appBarLayout: AppBarLayout) {
        _targetOffset = -(collapsingToolbarLayout.height - toolbar.height)

        removeOnOffsetChangedListenerFunc = appBarLayout::removeOnOffsetChangedListener
        addOnOffsetChangedListenerFunc = appBarLayout::addOnOffsetChangedListener

        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        viewBehavior = (params.behavior as AppBarLayout.Behavior).apply {
            setDragCallback(
                object : AppBarLayout.Behavior.DragCallback() {
                    override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                        return scrollable
                    }
                },
            )
            isVerticalOffsetEnabled = scrollable
        }

    }
}
