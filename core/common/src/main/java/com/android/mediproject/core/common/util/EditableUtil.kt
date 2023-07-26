package com.android.mediproject.core.common.util

import android.text.Editable


fun Editable?.isNotEmpty(): Boolean {
    return this?.toString()?.length != 0
}
