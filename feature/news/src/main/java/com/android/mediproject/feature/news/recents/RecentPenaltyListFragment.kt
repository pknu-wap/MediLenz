package com.android.mediproject.feature.news.recents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.util.deepNavigate
import com.android.mediproject.core.common.util.navigateByDeepLink
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.navargs.RecallDisposalArgs
import com.android.mediproject.core.model.news.recall.RecallSaleSuspension
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.stateAsCollect
import com.android.mediproject.feature.news.databinding.FragmentRecentPenaltyListBinding
import dagger.hilt.android.AndroidEntryPoint
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class RecentPenaltyListFragment :
    BaseFragment<FragmentRecentPenaltyListBinding, RecentPenaltyListViewModel>(FragmentRecentPenaltyListBinding::inflate) {

    enum class ResultKey {
        RESULT_KEY, PENALTY_ID
    }

    override val fragmentViewModel: RecentPenaltyListViewModel by viewModels()
    lateinit var penaltyListAdapter: PenaltyListAdapter

    @Inject
    lateinit var medicineInfoMapper: MedicineInfoMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted {
                    recallDisposalList.stateAsCollect(headerView, noHistoryTextView).collect { uiState ->
                        handleUiState(uiState)
                    }
                }
                repeatOnStarted { eventFlow.collect { event -> handleEvent(event) } }
            }
            noHistoryTextView.text = medicineInfoMapper.getNoHistorySpan(requireContext())
        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        penaltyListAdapter = PenaltyListAdapter()
        binding.penaltyList.apply {
            setHasFixedSize(true)
            adapter = penaltyListAdapter
        }
    }

    private fun handleUiState(uiState: UiState<List<RecallSaleSuspension>>) {
        when (uiState) {
            is UiState.Error -> {}
            is UiState.Initial -> {}
            is UiState.Loading -> {}
            is UiState.Success -> {
                uiState.data.forEach { itemDto ->
                    itemDto.onClick = {
                        findNavController().deepNavigate(
                            "medilens://main/news_nav", RecallDisposalArgs(it.product),
                        )
                    }
                }
                penaltyListAdapter.submitList(uiState.data)
            }
        }
    }

    private fun handleEvent(event: RecentPenaltyListViewModel.PenaltyListEvent) {
        when (event) {
            is RecentPenaltyListViewModel.PenaltyListEvent.NavigateToNews -> navigateToNews()
        }
    }

    private fun navigateToNews() {
        findNavController().navigateByDeepLink(
            "medilens://main/news_nav", RecallDisposalArgs(""),
        )
    }
}
