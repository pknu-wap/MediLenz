package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.uiutil.dpToPx

class FavoriteMedcineMoreDecoration(private val context : Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(0, dpToPx(context,10),0, dpToPx(context,10))

    }
}
