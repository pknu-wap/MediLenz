package com.android.mediproject.core.common.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import java.lang.ref.WeakReference

fun showLoadingDialog(context: Context, textMessage: String?): AlertDialog? {

    val progressIndicatorWeak = WeakReference<ProgressIndicator>(ProgressIndicator(context, null, textMessage))
    var progressIndicator = progressIndicatorWeak.get()

    val dialogReference = WeakReference(progressIndicator?.let {
        AlertDialog.Builder(context).setView(progressIndicator).setCancelable(false).create().also {
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            //width : 70% of screen, height : wrap content
            it.window?.attributes = it.window?.attributes?.apply {
                width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            it.setCanceledOnTouchOutside(false)
        }
    })

    return dialogReference.get()?.also { dialog ->
        dialog.setOnDismissListener {
            progressIndicatorWeak.clear()
            dialogReference.clear()
            progressIndicator = null
        }

        dialog.show()
    }

}