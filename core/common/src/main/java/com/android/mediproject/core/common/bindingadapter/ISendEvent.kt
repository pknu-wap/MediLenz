package com.android.mediproject.core.common.bindingadapter


fun interface ISendEvent<T> {
    fun send(e: T)
}