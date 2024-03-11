package com.android.mediproject.feature.comments.commentsofamedicine

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.isVisible
import com.android.mediproject.core.model.comments.BaseComment
import com.android.mediproject.feature.comments.databinding.ViewCommentInputBinding
import com.android.mediproject.feature.comments.util.SpanMapper
import io.github.pknujsp.simpledialog.SimpleDialogBuilder
import io.github.pknujsp.simpledialog.constants.DialogType
import io.github.pknujsp.simpledialog.dialogs.SimpleDialog

internal object CommentDialog {

    @SuppressLint("StaticFieldLeak") private var dialog: SimpleDialog? = null

    @SuppressLint("StaticFieldLeak") private var viewBinding: ViewCommentInputBinding? = null

    fun showComment(activity: Activity, viewModel: MedicineCommentsViewModel) {
        release()
        viewBinding = ViewCommentInputBinding.inflate(activity.layoutInflater)

        dialog = SimpleDialogBuilder.builder(activity, DialogType.BottomSheet).setLayoutSize(MATCH_PARENT, WRAP_CONTENT).setCancelable(true)
            .setCanceledOnTouchOutside(false).setCornerRadius(topStart = 12, topEnd = 12, bottomStart = 0, bottomEnd = 0)
            .setContentView(viewBinding!!.root).buildAndShow()

        viewBinding?.apply {
            replyContainer.isVisible = false
        }
    }

    fun showReply(activity: Activity, viewModel: MedicineCommentsViewModel, baseComment: BaseComment) {
        showComment(activity, viewModel)
        viewBinding?.apply {
            baseComment.run {
                baseCommentTextView.text = content
                // 이름, 등록 시각
                baseCommentInfoTextView.text = SpanMapper.createReplyToBaseCommentInfo(activity, baseComment)
            }
            replyContainer.isVisible = true
        }
    }

    private fun release() {
        dialog?.dismiss()
        dialog = null
        viewBinding = null
    }
}
