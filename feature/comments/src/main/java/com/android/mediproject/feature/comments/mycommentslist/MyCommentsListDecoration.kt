package com.android.mediproject.feature.comments.mycommentslist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.util.dpToPx

class MyCommentsListDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        //좌,상,우,하
        outRect.set(0, 15.dpToPx(), 0, 15.dpToPx())

    }
}
