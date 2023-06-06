package com.android.mediproject.feature.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.recentcommentlist.RecentCommentListFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import com.android.mediproject.feature.penalties.recentpenaltylist.RecentPenaltyListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    override val fragmentViewModel by viewModels<HomeViewModel>()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.WHITE, SystemBarStyler.NavigationBarColor.BLACK)
    }

    override fun onDetach() {
        super.onDetach()
        systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.WHITE, SystemBarStyler.NavigationBarColor.BLACK)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(binding.homeBar1, SystemBarStyler.SpacingType.PADDING),
            SystemBarStyler.ChangeView(binding.homeBar2, SystemBarStyler.SpacingType.PADDING)), emptyList())

        initSearchBar()
        initChildFragments()

        binding.apply {
            viewModel = fragmentViewModel

            homeBar1.bringToFront()
            homeBar2.bringToFront()

            /**
             * 일반 적인 경우에는 emasredHeight로 그냥 측정되지만, 현재 headerLayout에 크기가 wrap_content이며,
             * headerLayout의 부모뷰인 ConstrainyLayout도 wrap_content라서 UNSPECIFED모드로 측정해주어야 합니다.
             * 다른 모드들은 전부 0으로 측정됩니다.
             */

            headerLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val initialHeight = (headerLayout.measuredHeight * 1.2).toFloat()
            log(initialHeight.toString())

            scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                log(scrollY.toString())
                log((scrollY / initialHeight).toString())
                binding.homeBar2.alpha = (scrollY / initialHeight)
                if (binding.homeBar2.alpha > 0.7) systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.BLACK,
                    SystemBarStyler.NavigationBarColor.BLACK)
                else systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.WHITE, SystemBarStyler.NavigationBarColor.BLACK)
            }

        }

        fragmentViewModel.createHeaderText(getString(R.string.headerTextOnHome))
    }


    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnBarClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment())
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
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment(bundle.getString(
                    RecentSearchListFragment.ResultKey.WORD.name) ?: ""))
            }
        }
    }
}