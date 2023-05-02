package com.android.mediproject.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.recentcommentlist.RecentCommentListFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import com.android.mediproject.feature.penalties.recentpenaltylist.RecentPenaltyListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    override val fragmentViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBar()
        initChildFragments()
    }

    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnBarClickListener {
            findNavController().apply {
                navigate(HomeFragmentDirections.actionHomeFragmentToSearchMedicineHostNav())
            }
        }
    }

    private fun initChildFragments() {
        // 아이템 클릭 시 처리 로직
        childFragmentManager.apply {
            setFragmentResultListener(RecentCommentListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
                bundle.apply {
                    val result = getInt(RecentCommentListFragment.ResultKey.WORD.name)
                }
            }
            setFragmentResultListener(RecentPenaltyListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
                bundle.apply {
                    val result = getInt(RecentPenaltyListFragment.ResultKey.PENALTY_ID.name)
                }
            }
            setFragmentResultListener(RecentSearchListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
                bundle.apply {
                    val result = getInt(RecentSearchListFragment.ResultKey.WORD.name)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManualSearchResultNav())
                }
            }
        }
    }
}