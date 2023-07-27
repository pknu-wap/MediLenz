package com.android.mediproject.core.model.dur.duringr.ui

import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.core.model.dur.DurType

abstract class DurIngrItem(
    val durType: DurType,
) : DurItem {
    open val prohibitContent: String = ""
}
