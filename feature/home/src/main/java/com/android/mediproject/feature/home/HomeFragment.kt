package com.android.mediproject.feature.home

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

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    private fun setBarScrollEffect() = binding.apply {
        homeBar1.bringToFront()
        homeBar2.bringToFront()

        val headerLayoutHeight = getViewHeightInPercent(binding.headerLayout, 1.2F)

        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.homeBar2.alpha = (scrollY / headerLayoutHeight)

            if (binding.homeBar2.alpha > 0.7) systemBarStyler.setStyle(
                SystemBarStyler.SystemBarColor.BLACK,
                SystemBarStyler.SystemBarColor.BLACK,
            )
            else systemBarStyler.setStyle(SystemBarStyler.SystemBarColor.WHITE, SystemBarStyler.SystemBarColor.BLACK)
        }
    }

    private fun getViewHeightInPercent(view: View, percent: Float): Float {
        return measureViewHeight(view) * percent
    }

    private fun measureViewHeight(view: View): Int {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return view.measuredHeight
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            viewModel = fragmentViewModel
            fragmentViewModel.createHeaderText(getString(R.string.headerTextOnHome))
        }
        initSearchBar()
        initChildFragments()
        setBarStyle()
    }

    private fun setBarStyle() {
        systemBarStyler.changeMode(
            listOf(
                SystemBarStyler.ChangeView(binding.homeBar1, SystemBarStyler.SpacingType.PADDING),
                SystemBarStyler.ChangeView(binding.homeBar2, SystemBarStyler.SpacingType.PADDING),
                SystemBarStyler.ChangeView(binding.headerLayout, SystemBarStyler.SpacingType.PADDING),
            ),
            emptyList(),
        )
    }

    private fun initSearchBar() {
        binding.searchView.setOnBarClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment())
        }
    }

    private fun initChildFragments() {
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
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment(
                        bundle.getString(
                            RecentSearchListFragment.ResultKey.WORD.name,
                        ) ?: "",
                    ),
                )
            }
        }
    }
}
