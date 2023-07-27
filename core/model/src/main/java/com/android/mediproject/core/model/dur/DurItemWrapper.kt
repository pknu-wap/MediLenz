package com.android.mediproject.core.model.dur

import com.android.mediproject.core.model.DataGoKrResponse

abstract class DurItemWrapper(
    open val response: DataGoKrResponse<*>,
) {
    abstract fun convert(): List<DurItem>
}
