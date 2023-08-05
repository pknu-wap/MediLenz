package com.android.mediproject.core.common

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow


inline fun <T> SharedFlow<T>.collectWithDialog(message: String, lifecycleOwner: LifecycleOwner, crossinline action: suspend (T) -> Unit) {

}