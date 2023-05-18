package com.android.mediproject.feature.penalties.recentpenaltylist

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.penalties.databinding.FragmentRecentPenaltyListBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHeader()
        binding.apply {
            binding.headerView.onIndicatorVisibilityChanged(View.VISIBLE)

            penaltyList.setHasFixedSize(true)

            val penaltyListAdapter = PenaltyListAdapter()
            penaltyList.adapter = penaltyListAdapter

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.recallDisposalList.collect { uiState ->
                    when (uiState) {
                        is UiState.Error -> {}

                        is UiState.Initial -> {}

                        is UiState.Loading -> {}

                        is UiState.Success -> {
                            penaltyListAdapter.submitList(uiState.data)
                            binding.headerView.onIndicatorVisibilityChanged(View.GONE)
                            binding.headerView.setExpand(true)
                        }
                    }
                }
            }

        }
    }


    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.headerView.setOnExpandClickListener {}

        binding.headerView.setOnMoreClickListener {
            findNavController().navigate("medilens://main/news_nav".toUri())
        }
    }
}