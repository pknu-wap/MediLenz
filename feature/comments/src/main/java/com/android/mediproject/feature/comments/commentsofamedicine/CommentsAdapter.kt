package com.android.mediproject.feature.comments.commentsofamedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.comments.R
import com.android.mediproject.feature.comments.databinding.ItemViewCommentEditBinding
import com.android.mediproject.feature.comments.view.CommentItemView


class CommentsAdapter : PagingDataAdapter<CommentDto, CommentsAdapter.BaseCommentViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewType.let {
        when (it) {
            CommentType.COMMENT.ordinal -> CommentsViewHolder(CommentItemView(parent.context))
            CommentType.EDITING.ordinal -> CommentEditViewHolder(ItemViewCommentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseCommentViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    /**
     * 현재 댓글이 수정 중인 상태이면 1, 아니면 0 반환
     */
    override fun getItemViewType(position: Int): Int = getItem(position)?.run {
        if (isEditing) CommentType.EDITING.ordinal else CommentType.COMMENT.ordinal
    } ?: CommentType.COMMENT.ordinal


    /**
     * 댓글 아이템 뷰 홀더
     *
     * type : 0
     * 댓글 아이템 뷰
     */
    class CommentsViewHolder(private val view: CommentItemView) : BaseCommentViewHolder(view.rootView) {
        private var item: CommentDto? = null

        init {
            view.setOnReplyClickListener {
                item?.apply {
                    onClickReply?.invoke(commentId)
                }
            }
            view.setOnLikeClickListener {
                item?.apply {
                    onClickLike?.invoke(commentId)
                }
            }
            view.setOnMoreClickListener {
                item?.apply {
                    if (isMine) {
                        // 내 댓글이면 삭제, 수정 메뉴를 보여준다.
                        MediPopupMenu.showMenu(it, R.menu.comment_action_menu) { menuItem ->
                            when (menuItem.itemId) {
                                R.id.deleteMyComment -> onClickDelete?.invoke(absoluteAdapterPosition)
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

        override fun bind(commentDto: CommentDto) {
            item = commentDto
            view.setComment(commentDto)

            if (commentDto.isMine) view.moreButtonVisible(true)
        }

        override fun fixBind() {
        }
    }


    /**
     * 댓글 수정 뷰홀더
     *
     * type : 1
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

        override fun bind(commentDto: CommentDto) {
            binding.commentDto = commentDto
            binding.executePendingBindings()
        }

        override fun fixBind() {

        }
    }

    abstract class BaseCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(commentDto: CommentDto) {}

        /**
         * 뷰홀더가 재활용 될 때 호출하는 메소드
         */
        open fun fixBind() {}
    }

    object Diff : DiffUtil.ItemCallback<CommentDto>() {
        override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean = oldItem.commentId == newItem.commentId


        override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean = oldItem == newItem

    }

    /**
     * 아이템 뷰 타입
     * 0 : 댓글
     * 1 : 내 댓글 수정 중
     */
    enum class CommentType {
        COMMENT, EDITING
    }
}