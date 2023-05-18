package com.android.mediproject.core.common.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

object LoadingDialog {

    private var dialog: AlertDialog? = null

    fun showLoadingDialog(context: Context, textMessage: String?) {
        dismiss()
        AlertDialog.Builder(context).setView(ProgressIndicator(context, null, textMessage)).setCancelable(false).create().also {
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.attributes = it.window?.attributes?.apply {
                width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            it.setCanceledOnTouchOutside(false)
            dialog = it
            it.show()
        }

    }


    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}