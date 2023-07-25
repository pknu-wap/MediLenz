package com.android.mediproject.feature.penalties.recentpenaltylist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.util.deepNavigate
import com.android.mediproject.core.common.util.navigateByDeepLink
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.local.navargs.RecallDisposalArgs
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListItemDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.stateAsCollect
import com.android.mediproject.feature.penalties.databinding.FragmentRecentPenaltyListBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
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
        initHeader()
        setBinding()
    }


    private fun setRecyclerView() {
        penaltyListAdapter = PenaltyListAdapter()
        binding.penaltyList.apply {
            setHasFixedSize(true)
            adapter = penaltyListAdapter
        }
    }

    private fun handleUiState(uiState: UiState<List<RecallSuspensionListItemDto>>) {
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

    private fun setBinding() {
        binding.apply {
            fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted {
                        recallDisposalList.stateAsCollect(headerView, noHistoryTextView).collect { uiState ->
                            handleUiState(uiState)
                        }
                    }
                    repeatOnStarted {
                        eventFlow.collect { event ->
                            handleEvent(event)
                        }
                    }
                }
                noHistoryTextView.text = medicineInfoMapper.getNoHistorySpan(requireContext())
            }
        }
        setRecyclerView()
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
