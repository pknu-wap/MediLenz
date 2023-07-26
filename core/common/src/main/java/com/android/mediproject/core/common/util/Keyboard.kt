package com.android.mediproject.core.common.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * 키보드를 숨기는 함수
 * @return Unit
 */
fun Fragment.hideKeyboard() {
    activity?.also {
        // 현재 포커스가 있는 뷰를 찾는다.
        if (it.currentFocus != null) {
            // 키보드 숨기기
            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                it.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS,
            )
        }
    }
}
