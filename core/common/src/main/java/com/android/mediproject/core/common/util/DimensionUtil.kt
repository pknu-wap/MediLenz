package com.android.mediproject.core.common.util

import android.content.res.Resources
import android.util.TypedValue


fun Int.dpToPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics,
).toInt()

fun Int.spToPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics,
).toInt()
