package com.android.mediproject.core.common.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object LoadingDialog {

    private var dialog: AlertDialog? = null

    fun showLoadingDialog(context: Context, textMessage: String?) {
        dismiss()
        MaterialAlertDialogBuilder(context).setView(ProgressIndicator(context, textMessage))
            .setCancelable(false).create().also {
                it.setCanceledOnTouchOutside(false)
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dismiss()
                dialog = it
                it.show()
            }

    }


    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}