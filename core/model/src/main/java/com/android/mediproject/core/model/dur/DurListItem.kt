package com.android.mediproject.core.model.dur

import android.text.Spanned

class DurListItem(
    val durType: DurType,
    val title: Spanned,
    val description: Spanned,
) {
    var durItems: Result<List<DurItem>>? = null
}
