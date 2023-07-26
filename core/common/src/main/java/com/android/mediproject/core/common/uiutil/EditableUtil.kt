package com.android.mediproject.core.common.uiutil

import android.text.Editable


fun Editable?.isNotEmpty(): Boolean {
    return this?.toString()?.length != 0
}
