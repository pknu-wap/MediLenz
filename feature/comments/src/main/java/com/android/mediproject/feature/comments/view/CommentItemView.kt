package com.android.mediproject.feature.comments.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.android.mediproject.core.common.uiutil.dpToPx
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.feature.comments.R
import java.time.format.DateTimeFormatter

class CommentItemView(
    context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    private val userProfileImageView: ImageView
    private val userNameTextView: TextView
    private val replyButton: ImageView
    private val likeButton: ImageView
    private val commentTextView: TextView
    private val dateTimeTextView: TextView

    init {

        id = R.id.commentItemView
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setPadding(dpToPx(context, 12), dpToPx(context, 4), dpToPx(context, 12), dpToPx(context, 4))

        userProfileImageView = ImageView(context).apply {
            id = R.id.userProfileImageView
            layoutParams = LayoutParams(dpToPx(context, 24), dpToPx(context, 24))
            setImageResource(com.android.mediproject.core.ui.R.drawable.logo)
        }

        userNameTextView = TextView(context).apply {
            id = R.id.userNameTextView
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            ellipsize = TextUtils.TruncateAt.MARQUEE
            maxLines = 1
            text = "userName"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(Color.BLACK)
            gravity = Gravity.START
        }

        val selectableBackgroundValue = TypedValue().apply {
            context.theme.resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, this, true)
        }.resourceId

        replyButton = ImageView(context).apply {
            id = R.id.replyButton
            layoutParams = LayoutParams(dpToPx(context, 24), dpToPx(context, 24))
            setBackgroundResource(selectableBackgroundValue)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.replyButtonColor)
            isClickable = true
            contentDescription = context.getString(R.string.replyComment)
            setImageResource(R.drawable.outline_add_comment_24)
        }

        likeButton = ImageView(context).apply {
            id = R.id.likeButton
            layoutParams = LayoutParams(dpToPx(context, 24), dpToPx(context, 24))
            setBackgroundResource(selectableBackgroundValue)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.likeButtonColor)
            isClickable = true
            contentDescription = context.getString(R.string.likeComment)
            setImageResource(R.drawable.outline_thumb_up_24)
        }

        commentTextView = TextView(context).apply {
            id = R.id.commentTextView
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(0, dpToPx(context, 9), 0, 0)
            text = "comment"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        }

        dateTimeTextView = TextView(context).apply {
            id = R.id.dateTimeTextView
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(0, dpToPx(context, 4), 0, 0)
            text = "dateTime"
            setTextColor(Color.GRAY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }

        addViews()
        applyConstraints()
    }

    private fun addViews() {
        addView(userProfileImageView)
        addView(userNameTextView)
        addView(replyButton)
        addView(likeButton)
        addView(commentTextView)
        addView(dateTimeTextView)
    }

    private fun applyConstraints() {
        ConstraintSet().also { constraintSet ->

            constraintSet.clone(this)

            constraintSet.connect(
                userProfileImageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT
            )
            constraintSet.connect(
                userProfileImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )

            constraintSet.connect(
                userNameTextView.id, ConstraintSet.LEFT, userProfileImageView.id, ConstraintSet.RIGHT, dpToPx(context, 7)
            )
            constraintSet.connect(
                userNameTextView.id, ConstraintSet.TOP, userProfileImageView.id, ConstraintSet.TOP
            )
            constraintSet.connect(
                userNameTextView.id, ConstraintSet.BOTTOM, userProfileImageView.id, ConstraintSet.BOTTOM
            )
            constraintSet.connect(
                userNameTextView.id, ConstraintSet.RIGHT, likeButton.id, ConstraintSet.LEFT
            )

            constraintSet.connect(
                replyButton.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT
            )
            constraintSet.connect(
                replyButton.id, ConstraintSet.TOP, userProfileImageView.id, ConstraintSet.TOP
            )
            constraintSet.connect(
                replyButton.id, ConstraintSet.BOTTOM, userProfileImageView.id, ConstraintSet.BOTTOM
            )

            constraintSet.connect(
                likeButton.id, ConstraintSet.RIGHT, replyButton.id, ConstraintSet.LEFT, dpToPx(context, 16)
            )
            constraintSet.connect(
                likeButton.id, ConstraintSet.TOP, userProfileImageView.id, ConstraintSet.TOP
            )
            constraintSet.connect(
                likeButton.id, ConstraintSet.BOTTOM, userProfileImageView.id, ConstraintSet.BOTTOM
            )

            constraintSet.connect(
                commentTextView.id, ConstraintSet.TOP, userProfileImageView.id, ConstraintSet.BOTTOM, dpToPx(context, 9)
            )
            constraintSet.connect(
                commentTextView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT
            )
            constraintSet.connect(
                commentTextView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT
            )

            constraintSet.connect(
                dateTimeTextView.id, ConstraintSet.TOP, commentTextView.id, ConstraintSet.BOTTOM, dpToPx(context, 4)
            )
            constraintSet.connect(
                dateTimeTextView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT
            )
            constraintSet.connect(
                dateTimeTextView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT
            )

            constraintSet.applyTo(this)
        }

    }


    /*
    fun1 : set profileimage, username, comment, datetime
     */
    fun setComment(comment: CommentDto) {
        comment.apply {
            userNameTextView.text = userName
            commentTextView.text = content
            dateTimeTextView.text = dateTime
        }
    }
}