package com.android.mediproject.core.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    fun log(str: String) = Log.d("wap", str) //for test
}
