package com.android.mediproject.core.common.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

object ProgressDialog {

    fun createDialog(context: Context, textMessage: String?): AlertDialog {
        val progressIndicator = ProgressIndicator(context, null, textMessage)
        return AlertDialog.Builder(context).setView(progressIndicator).setCancelable(false).create().let {
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                it.setCanceledOnTouchOutside(false)
                it
            }
    }
}