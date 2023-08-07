package com.android.mediproject.core.model.common

abstract class UiModelWrapper<OUT : UiModel>(
) {
    abstract val source: Any

    abstract fun convert(): List<OUT>
}
