package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.util.AttributeSet
import com.android.mediproject.core.ui.R
import com.google.android.material.chip.Chip

/**
 * Chip을 Button처럼 사용하기 위한 CustomView
 *
 * 예를 들어 최근 검색 목록에서 검색어를 보여주는 버튼 뷰입니다.
 *
 * 클릭 시 해당 내용을 가진 다음 화면으로 이동이 가능합니다.
 */
class ButtonChip constructor(
    context: Context, attrs: AttributeSet
) : Chip(
    context, attrs, R.style.ButtonChip
) {}