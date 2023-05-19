package com.android.mediproject.feature.comments.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.mediproject.core.common.uiutil.dpToPx
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.feature.comments.R
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

class CommentItemView(
    context: Context
) : ConstraintLayout(context, null) {

    companion object {
        private val dateTimeFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") }
    }

    private val userProfileImageView: ImageView
    private val userNameTextView: TextView
    private val replyButton: ImageView
    private val likeButton: ImageView
    private val moreButton: ImageView
    private val commentTextView: TextView
    private val dateTimeTextView: TextView


    init {

        val selectableBackgroundValue = TypedValue().apply {
            context.theme.resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, this, true)
        }.resourceId

        id = R.id.commentItemView
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dpToPx(context, 11).apply {
            setPadding(this, this, this, this)
        }

        userProfileImageView = ImageView(context).apply {
            id = R.id.userProfileImageView
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24)).apply {
                leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            setImageResource(com.android.mediproject.core.ui.R.drawable.logo)
        }

        moreButton = ImageView(context).apply {
            id = R.id.moreButton
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24)).apply {
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = userProfileImageView.id
                bottomToBottom = userProfileImageView.id
            }

            setBackgroundResource(selectableBackgroundValue)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.moreButtonColor)
            isClickable = true
            contentDescription = context.getString(R.string.more)
            setImageResource(com.android.mediproject.core.ui.R.drawable.baseline_more_vert_24)
        }

        replyButton = ImageView(context).apply {
            id = R.id.replyButton
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24)).apply {
                endToStart = moreButton.id
                topToTop = userProfileImageView.id
                bottomToBottom = userProfileImageView.id
                marginEnd = dpToPx(context, 16)
            }

            setBackgroundResource(selectableBackgroundValue)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.replyButtonColor)
            isClickable = true
            contentDescription = context.getString(R.string.replyComment)
            setImageResource(R.drawable.outline_add_comment_24)
        }

        likeButton = ImageView(context).apply {
            id = R.id.likeButton
            layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24)).apply {
                endToStart = replyButton.id
                topToTop = userProfileImageView.id
                bottomToBottom = userProfileImageView.id
                marginEnd = dpToPx(context, 16)
            }
            setBackgroundResource(selectableBackgroundValue)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.likeButtonColor)
            isClickable = true
            contentDescription = context.getString(R.string.likeComment)
            setImageResource(R.drawable.outline_thumb_up_24)
        }

        userNameTextView = TextView(context).apply {
            id = R.id.userNameTextView
            layoutParams = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
                startToEnd = userProfileImageView.id
                topToTop = userProfileImageView.id
                bottomToBottom = userProfileImageView.id
                endToStart = likeButton.id
                marginStart = dpToPx(context, 7)
            }

            ellipsize = TextUtils.TruncateAt.MARQUEE
            maxLines = 1
            text = "userName"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(Color.BLACK)
            gravity = Gravity.START
        }

        commentTextView = TextView(context).apply {
            id = R.id.commentTextView
            layoutParams =
                ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        topToBottom = userProfileImageView.id
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        topMargin = dpToPx(context, 9)
                    }
            setPadding(0, dpToPx(context, 9), 0, 0)
            text = "comment"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        }

        dateTimeTextView = TextView(context).apply {
            id = R.id.dateTimeTextView
            layoutParams =
                ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        topToBottom = commentTextView.id
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        topMargin = dpToPx(context, 4)
                    }
            setPadding(0, dpToPx(context, 4), 0, 0)
            text = "dateTime"
            setTextColor(Color.GRAY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }

        addView(userProfileImageView)
        addView(userNameTextView)
        addView(replyButton)
        addView(likeButton)
        addView(commentTextView)
        addView(dateTimeTextView)
    }


    /**
     * 뷰 배경을 댓글 종속성에 따라 지정합니다.
     */
    private fun setCommentBackground(isReply: Boolean) {
        setBackgroundResource(
            if (isReply) R.drawable.reply_background
            else R.drawable.comment_background
        )
    }


    /**
     * 댓글 정보를 뷰에 적용합니다.
     *
     * @param comment 댓글 정보
     */
    fun setComment(comment: CommentDto) {
        comment.apply {
            setCommentBackground(isReply)

            userNameTextView.text = userName
            commentTextView.text = content
            dateTimeTextView.text = createdAt.toJavaLocalDateTime().format(dateTimeFormatter)
        }
    }


    /**
     * 답글 작성 버튼 클릭 콜백 추가
     */
    fun setOnReplyClickListener(onReplyClickListener: OnClickListener) {
        replyButton.setOnClickListener {
            onReplyClickListener.onClick(it)
        }
    }

    /**
     * 좋아요 버튼 클릭 콜백 추가
     */
    fun setOnLikeClickListener(onLikeClickListener: OnClickListener) {
        likeButton.setOnClickListener {
            onLikeClickListener.onClick(it)
        }
    }

    /**
     * 내 댓글 삭제 버튼 클릭 콜백 추가
     */
    fun setOnDeleteClickListener(onDeleteClickListener: OnClickListener) {
        TODO()
    }

    /**
     * 더 보기 버튼
     */
    fun setOnMoreClickListener(onMoreClickListener: OnClickListener) {
        moreButton.setOnClickListener {
            onMoreClickListener.onClick(it)
        }
    }

}