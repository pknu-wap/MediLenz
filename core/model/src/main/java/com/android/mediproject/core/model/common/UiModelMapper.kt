package com.android.mediproject.core.model.common

import com.android.mediproject.core.model.servercommon.NetworkApiResponse

abstract class UiModelMapper<OUT : UiModel> {
    abstract val source: NetworkApiResponse.ListItem
    abstract fun convert(): OUT
}
