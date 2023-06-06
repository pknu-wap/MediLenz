package com.android.mediproject.core.ui.base

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    fun log(str: String) = Log.e("wap", str) //for test
}