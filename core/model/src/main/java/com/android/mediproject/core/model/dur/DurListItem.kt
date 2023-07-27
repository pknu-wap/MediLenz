package com.android.mediproject.core.model.dur

class DurListItem(
    val durType: DurType,
) {
    var durItems: Result<List<DurItem>>? = null
}
