package com.android.mediproject.core.common.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import java.lang.ref.WeakReference


fun showLoadingDialog(context: Context, textMessage: String?): AlertDialog {

    val progressIndicator = ProgressIndicator(context, null, textMessage)
    return AlertDialog.Builder(context).setView(progressIndicator).setCancelable(false).create().let {
        it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        //width : 70% of screen, height : wrap content
        it.window?.attributes = it.window?.attributes?.apply {
            width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        it.setCanceledOnTouchOutside(false)
        val weakReference = WeakReference(it)

        it.setOnDismissListener {
            weakReference.clear()
        }

        it.show()
        it
    }
}