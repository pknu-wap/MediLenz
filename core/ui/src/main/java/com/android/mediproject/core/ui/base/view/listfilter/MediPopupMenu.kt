package com.android.mediproject.core.ui.base.view.listfilter

import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener

/**
 * Filter 버튼 클릭 시 나오는 팝업 메뉴를 생성할 수 있는 클래스입니다.
 */
object MediPopupMenu {

    /**
     * [v]에 해당하는 View에 [menuRes]에 해당하는 메뉴를 보여줍니다.
     *
     * @param v 메뉴를 보여줄 View
     * @param menuRes 보여줄 메뉴의 리소스
     * @param onMenuItemClickListener 메뉴 아이템 클릭 리스너
     */
    fun showMenu(v: View, @MenuRes menuRes: Int, onMenuItemClickListener: OnMenuItemClickListener) {
        PopupMenu(v.context, v).apply {
            menuInflater.inflate(menuRes, menu)

            setOnMenuItemClickListener { item: MenuItem ->
                onMenuItemClickListener.onMenuItemClick(item)
                true
            }

            show()
        }

    }
}