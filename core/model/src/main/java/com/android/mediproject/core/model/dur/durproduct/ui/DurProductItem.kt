package com.android.mediproject.core.model.dur.durproduct.ui

import com.android.mediproject.core.model.dur.DurItem
import com.android.mediproject.core.model.dur.DurType

abstract class DurProductItem(
    val durType: DurType,
) : DurItem {
    open val prohibitContent: String = ""
}
