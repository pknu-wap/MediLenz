package com.android.mediproject.feature.search.recentsearchlist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.search.databinding.FragmentRecentSearchListBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint


/**
 * 최근 검색 목록 프래그먼트
 *
 * Material3 Chip이용하고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentSearchListFragment :
    BaseFragment<FragmentRecentSearchListBinding, RecentSearchListViewModel>(FragmentRecentSearchListBinding::inflate) {

    enum class ResultKey {
        SEARCH_HISTORY_QUERY_KEY, WORD
    }

    override val fragmentViewModel: RecentSearchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addHistoryItemChips()
        initHeader()
    }

    /**
     * 최근 검색 목록 Chip을 추가합니다.
     *
     * 클릭 시 관련 로직을 수행하도록 합니다.
     */
    private fun addHistoryItemChips() {
        binding.apply {
            val horizontalSpace = resources.getDimension(com.android.mediproject.core.ui.R.dimen.dp_4).toInt()

            repeat(5) {
                this.searchHistoryList.addView(ButtonChip<Int>(requireContext()).apply {
                    layoutParams = FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(horizontalSpace, 0, horizontalSpace, 0)
                    }
                    data = it
                    setChipText("검색어 $it")
                    setOnChipClickListener {
                        it?.apply {
                            setFragmentResult(ResultKey.SEARCH_HISTORY_QUERY_KEY.name, bundleOf(ResultKey.WORD.name to it))
                        }
                    }
                })
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

        binding.headerView.setOnMoreClickListener {}
    }
}