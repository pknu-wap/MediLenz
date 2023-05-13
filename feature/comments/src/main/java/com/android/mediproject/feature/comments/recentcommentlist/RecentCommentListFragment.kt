package com.android.mediproject.feature.comments.recentcommentlist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.SimpleListItemView
import com.android.mediproject.feature.comments.databinding.FragmentRecentCommentListBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * 최근 댓글 목록 프래그먼트
 *
 * Material3 Chip으로 의약품 명 보여주고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentCommentListFragment :
    BaseFragment<FragmentRecentCommentListBinding, RecentCommentListViewModel>(FragmentRecentCommentListBinding::inflate) {
    enum class ResultKey {
        RESULT_KEY, WORD
    }

    override val fragmentViewModel: RecentCommentListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //  val commentListAdapter = RecentCommentListAdapter()
            // commentList.adapter = commentListAdapter
        }
        // addCommentItems()
        initHeader()
    }


    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.headerView.setOnExpandClickListener {}

        binding.headerView.setOnMoreClickListener {}
    }
}


class RecentCommentListAdapter : RecyclerView.Adapter<RecentCommentListAdapter.RecentCommentListViewHolder>() {

    private val mDiffer = AsyncListDiffer<Any>(this, object : DiffUtil.ItemCallback<Any>(
    ) {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            TODO()
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            TODO()
        }
    })


    class RecentCommentListViewHolder(private val view: SimpleListItemView<Any>) : ViewHolder(view) {

        private var item: Any? = null

        init {
            view.setOnItemClickListener {
                it?.also {
                    // 데이터 클래스 내 람다로 처리
                }
            }
        }

        fun bind(data: Any?) {
            data?.apply {
                item = this
                view.setTitle("Title")
                view.setContent("Content")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCommentListAdapter.RecentCommentListViewHolder {
        return RecentCommentListAdapter.RecentCommentListViewHolder(
            SimpleListItemView<Any>(parent.context)
        )
    }

    override fun onBindViewHolder(holder: RecentCommentListAdapter.RecentCommentListViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    fun submitList(list: List<Any>) {
        mDiffer.submitList(list)
    }


}