package com.android.mediproject.core.common.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

object LoadingDialog {

    private var dialog: AlertDialog? = null

    fun showLoadingDialog(context: Context, textMessage: String?) {
        dismiss()
        AlertDialog.Builder(context).setView(ProgressIndicator(context, textMessage)).setCancelable(false).create().also {
            it.window?.apply {
                setClipToOutline(true)
                setElevation(8f)
                // shadow

                attributes = attributes.apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    dimAmount = 0.5f
                }
            }
            it.setCanceledOnTouchOutside(false)
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