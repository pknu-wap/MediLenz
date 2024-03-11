package com.android.mediproject.feature.intro

import android.app.Activity
import com.android.mediproject.feature.intro.databinding.DialogEmailVerificationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun emailVerificationDialog(activity: Activity, verifyCode: (String) -> Unit, resendCode: () -> Unit) {
    val binding = DialogEmailVerificationBinding.inflate(activity.layoutInflater, null, false)
    val dialogBuilder = MaterialAlertDialogBuilder(activity).apply {
        setView(binding.root)
    }

    binding.apply {

    }
    dialogBuilder.create().show()
}
