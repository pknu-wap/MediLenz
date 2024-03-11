package com.android.mediproject.feature.intro

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun emailVerificationDialog(activity: Activity, verifyCode: (String) -> Unit, resendCode: () -> Unit) {
    val dialogBuilder = MaterialAlertDialogBuilder(activity).apply {
        _binding = FragmentMyPageMoreDialogBinding.inflate(layoutInflater, null, false)
        setView(onCreateView(layoutInflater, binding.rootLayout, savedInstanceState))
    }
}
