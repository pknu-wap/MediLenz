package com.android.mediproject.feature.comments.commentsofamedicine

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.android.mediproject.feature.comments.databinding.ViewCommentInputBinding
import io.github.pknujsp.simpledialog.SimpleDialogBuilder
import io.github.pknujsp.simpledialog.constants.DialogType
import io.github.pknujsp.simpledialog.dialogs.SimpleDialog

internal object CommentDialog {

    @SuppressLint("StaticFieldLeak") private var dialog: SimpleDialog? = null
    private var viewBinding: ViewCommentInputBinding? = null

    fun show(activity: Activity, viewModel: MedicineCommentsViewModel) {
        release()
        viewBinding = ViewCommentInputBinding.inflate(activity.layoutInflater)

        dialog = SimpleDialogBuilder.builder(activity, DialogType.BottomSheet).setLayoutSize(MATCH_PARENT, WRAP_CONTENT).setCancelable(true)
            .setCanceledOnTouchOutside(false).setCornerRadius(topStart = 12, topEnd = 12, bottomStart = 0, bottomEnd = 0)
            .setContentView(viewBinding!!.root).buildAndShow()
    }

    private fun release() {
        dialog?.dismiss()
        dialog = null
        viewBinding = null
    }
}
