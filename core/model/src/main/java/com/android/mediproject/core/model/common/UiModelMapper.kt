package com.android.mediproject.core.model.common

abstract class UiModelMapper<OUT : UiModel> {
    abstract val source: Any
    abstract fun convert(): OUT
}
