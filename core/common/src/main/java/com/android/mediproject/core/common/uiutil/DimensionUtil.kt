package com.android.mediproject.core.common.uiutil

import android.content.Context
import android.util.TypedValue

fun dpToPx(context: Context, dp: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
).toInt()

fun spToPx(context: Context, sp: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), context.resources.displayMetrics
).toInt()

fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Int.spToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics
).toInt()