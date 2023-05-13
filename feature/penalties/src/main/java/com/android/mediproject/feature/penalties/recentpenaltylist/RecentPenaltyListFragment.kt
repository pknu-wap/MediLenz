package com.android.mediproject.feature.penalties.recentpenaltylist

import android.graphics.Color
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
import com.android.mediproject.feature.penalties.databinding.FragmentRecentPenaltyListBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * 행정 처분 목록 프래그먼트
 *
 * Material3 Chip으로 의약품 명 보여주고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentPenaltyListFragment :
    BaseFragment<FragmentRecentPenaltyListBinding, RecentPenaltyListViewModel>(FragmentRecentPenaltyListBinding::inflate) {

    enum class ResultKey {
        RESULT_KEY, PENALTY_ID
    }

    override val fragmentViewModel: RecentPenaltyListViewModel by viewModels()
    private val penaltyListAdapter = PenaltyListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // this.penaltyList.adapter = penaltyListAdapter
        }
        // addPenaltyItems()
        initHeader()
    }

    private fun addPenaltyItems() {
        binding.apply {
            (0 until 5).map {
                it
            }.toList().apply {
                penaltyListAdapter.submitList(this)
            }
        }
    }

    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.headerView.setOnExpandClickListener {
        }

        binding.headerView.setOnMoreClickListener {
        }
    }
}

class PenaltyListAdapter : RecyclerView.Adapter<PenaltyListAdapter.PenaltyViewHolder>() {


    private val mDiffer = AsyncListDiffer<Any>(this, object : DiffUtil.ItemCallback<Any>(
    ) {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            TODO("Not yet implemented")
        }
    })


    class PenaltyViewHolder(private val view: SimpleListItemView<Any>) : ViewHolder(view) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenaltyListAdapter.PenaltyViewHolder {
        return PenaltyListAdapter.PenaltyViewHolder(SimpleListItemView<Any>(parent.context).apply {
            setTitleColor(Color.BLACK)
            setContentTextColor(Color.BLACK)
            setChipStrokeColor(com.android.mediproject.core.ui.R.color.black)
        })
    }

    override fun onBindViewHolder(holder: PenaltyListAdapter.PenaltyViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    fun submitList(list: List<Any>) {
        mDiffer.submitList(list)
    }
}