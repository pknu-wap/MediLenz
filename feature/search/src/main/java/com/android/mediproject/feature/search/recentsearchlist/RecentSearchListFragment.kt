package com.android.mediproject.feature.search.recentsearchlist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.core.os.bundleOf
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.search.local.SearchHistoryItemDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.core.ui.base.view.stateAsCollect
import com.android.mediproject.feature.search.databinding.FragmentRecentSearchListBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted


/**
 * 최근 검색 목록 프래그먼트
 *
 * Material3 Chip이용하고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentSearchListFragment :
    BaseFragment<FragmentRecentSearchListBinding, RecentSearchListViewModel>(
        FragmentRecentSearchListBinding::inflate
    ) {

    enum class ResultKey {
        RESULT_KEY, WORD
    }

    override val fragmentViewModel: RecentSearchListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()

        viewLifecycleOwner.repeatOnStarted {
            // 최근 검색 목록을 가져옵니다.
            fragmentViewModel.searchHistoryList().stateAsCollect(binding.headerView).collectLatest {
                when (it) {
                    is UiState.Success -> {
                        // 리스트를 비웁니다.
                        if (binding.searchHistoryList.size > 0) binding.searchHistoryList.removeAllViews()
                        it.data.forEach { searchHistoryItemDto ->
                            addHistoryItemChips(searchHistoryItemDto)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    /**
     * 최근 검색 목록 Chip을 추가합니다.
     *
     * 클릭 시 관련 로직을 수행하도록 합니다.
     */
    private fun addHistoryItemChips(searchHistoryItemDto: SearchHistoryItemDto) {
        binding.apply {
            val horizontalSpace =
                resources.getDimension(com.android.mediproject.core.ui.R.dimen.dp_4).toInt()
            this.searchHistoryList.addView(ButtonChip<String>(requireContext()).apply {
                layoutParams =
                    FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                        .apply {
                            setMargins(horizontalSpace, 0, horizontalSpace, 0)
                        }
                data = searchHistoryItemDto.query
                setChipText(data.toString())
                setOnChipClickListener {
                    onClicked(searchHistoryItemDto.query)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun onClicked(query: String) {
        parentFragmentManager.apply {
            setFragmentResult(ResultKey.RESULT_KEY.name, bundleOf(ResultKey.WORD.name to query))
        }
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