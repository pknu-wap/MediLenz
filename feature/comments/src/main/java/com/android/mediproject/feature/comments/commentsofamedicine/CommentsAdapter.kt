package com.android.mediproject.feature.comments.commentsofamedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.BaseComment
import com.android.mediproject.core.model.comments.Comment
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.ItemViewCommentBinding
import com.android.mediproject.feature.comments.databinding.ItemViewCommentEditBinding
import com.android.mediproject.feature.comments.databinding.ItemViewReplyBinding


class CommentsAdapter : PagingDataAdapter<Comment, CommentsAdapter.BaseCommentViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewType.let {
        when (it) {
            ViewType.COMMENT.ordinal -> CommentViewHolder(
                ItemViewCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )

            ViewType.REPLY.ordinal -> ReplyViewHolder(ItemViewReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            else -> CommentEditViewHolder(ItemViewCommentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseCommentViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    /**
     * 뷰 타입이 : 0, 1, 2
     * 댓글, 답글, 수정 중인 댓글
     */
    override fun getItemViewType(position: Int): Int = getItem(position)?.run {
        when {
            isEditing -> ViewType.EDITING.ordinal
            isReply -> ViewType.REPLY.ordinal
            else -> ViewType.COMMENT.ordinal
        }
    } ?: ViewType.COMMENT.ordinal


    /**
     * 댓글 아이템 뷰 홀더
     *
     * type : 0
     * 댓글 아이템 뷰
     */
    class CommentViewHolder(private val binding: ItemViewCommentBinding) : BaseCommentViewHolder(binding.root) {
        init {
            binding.apply {
                replyButton.setOnClickListener {
                    comment?.apply {
                        onClickReply?.invoke(UiModelMapperFactory.create<BaseComment>(this).convert())
                    }
                }
                likeButton.setOnClickListener {
                    comment?.apply {
                        onClickLike?.invoke(commentId, !isLiked)
                    }
                }
                moreButton.setOnClickListener {
                    comment?.apply {
                        if (isMine) {
                            // 내 댓글이면 삭제, 수정 메뉴를 보여준다.
                            MediPopupMenu.showMenu(it, R.menu.comment_action_menu) { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.deleteMyComment -> onClickDelete?.invoke(commentId)
                                    R.id.editMyComment -> {
                                        onClickEdit?.invoke(this, absoluteAdapterPosition)
                                    }
                                }
                                true
                            }
                        }
                    }
                }

            }
        }

        override fun bind(comment: Comment) {
            binding.apply {
                this.comment = comment
                moreButton.isVisible = comment.isMine
                likeButton.setBackgroundResource(if (comment.isLiked) R.drawable.baseline_thumb_up_24 else R.drawable.outline_thumb_up_24)
                executePendingBindings()
            }
        }
    }

    /**
     * 답글 아이템 뷰 홀더
     *
     * type : 1
     * 답글 아이템 뷰
     */
    class ReplyViewHolder(private val binding: ItemViewReplyBinding) : BaseCommentViewHolder(binding.root) {
        init {
            binding.apply {
                commentView.replyButton.setOnClickListener {
                    commentView.comment?.apply {
                        onClickReply?.invoke(UiModelMapperFactory.create<BaseComment>(this).convert())
                    }
                }
                commentView.likeButton.setOnClickListener {
                    commentView.comment?.apply {
                        onClickLike?.invoke(commentId, !isLiked)
                    }
                }
                commentView.moreButton.setOnClickListener {
                    commentView.comment?.apply {
                        if (isMine) {
                            // 내 댓글이면 삭제, 수정 메뉴를 보여준다.
                            MediPopupMenu.showMenu(it, R.menu.comment_action_menu) { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.deleteMyComment -> onClickDelete?.invoke(commentId)
                                    R.id.editMyComment -> {
                                        onClickEdit?.invoke(this, absoluteAdapterPosition)
                                    }
                                }
                                true
                            }
                        }
                    }
                }

            }
        }

        override fun bind(comment: Comment) {
            binding.apply {
                commentView.comment = comment
                commentView.moreButton.isVisible = comment.isMine
                commentView.likeButton.setBackgroundResource(if (comment.isLiked) R.drawable.baseline_thumb_up_24 else R.drawable.outline_thumb_up_24)
                commentView.executePendingBindings()
            }
        }

    }

    /**
     * 댓글 수정 뷰홀더
     *
     * type : 2
     * 나의 댓글을 수정 중일때 보여주는 아이템 뷰
     */
    class CommentEditViewHolder(private val binding: ItemViewCommentEditBinding) : BaseCommentViewHolder(binding.root) {
        init {
            binding.apply {
                commentEditButton.setOnClickListener {
                    takeIf { !binding.commentInput.text.isNullOrBlank() && commentDto != null }?.apply {
                        commentDto?.apply {
                            onClickApplyEdited?.invoke(copy(content = binding.commentInput.text.toString()))
                        }
                    }
                }

                cancelButton.setOnClickListener {
                    commentDto?.apply {
                        onClickEdit?.invoke(this, absoluteAdapterPosition)
                    }
                }
            }

        }

        override fun bind(comment: Comment) {
            binding.apply {
                this.commentDto = comment
                // 답글 인지 메인 댓글인지에 따라 배경을 다르게 함
                if (comment.isReply) root.setBackgroundResource(R.drawable.reply_background)
                else root.setBackgroundResource(R.drawable.comment_background)
                executePendingBindings()
            }
        }

    }

    abstract class BaseCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(comment: Comment) {}
    }

    object Diff : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem.commentId == newItem.commentId


        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem == newItem
    }

    /**
     * 아이템 뷰 타입
     * 0 : 댓글
     * 1 : 답글
     * 2 : 내 댓글 수정 중
     */
    enum class ViewType {
        COMMENT, REPLY, EDITING
    }
}
