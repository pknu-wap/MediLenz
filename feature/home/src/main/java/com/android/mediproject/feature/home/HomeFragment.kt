package com.android.mediproject.feature.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.recentcommentlist.RecentCommentListFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import com.android.mediproject.feature.penalties.recentpenaltylist.RecentPenaltyListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    companion object {
        private const val THRESHOLD = 0.7
    }

    override val fragmentViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    @Inject
    lateinit var medicineInfoMapper: MedicineInfoMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                }
            }
        }
        setBarStyle()
        setFragmentResultListener()
        setHeaderText()
    }

    private fun handleEvent(event: HomeViewModel.HomeEvent) {
        when (event) {
            is HomeViewModel.HomeEvent.NavigateToSearch -> navigateWithNavDirections(HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment())
        }
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
        setBarScrollEffect()
    }

    private fun setBarScrollEffect() = binding.apply {
        homeBar1.bringToFront()
        homeBar2.bringToFront()

        val headerLayoutHeight = getViewHeightInPercent(binding.headerLayout, 1.2F)

        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            homeBar2.alpha = (scrollY / headerLayoutHeight)
            setScrollBarStyle()
        }
    }

    private fun getViewHeightInPercent(view: View, percent: Float): Float {
        return measureViewHeight(view) * percent
    }

    private fun measureViewHeight(view: View): Int {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return view.measuredHeight
    }

    private fun setScrollBarStyle() = binding.apply {
        if (homeBar2.alpha > THRESHOLD) {
            systemBarStyler.setStyle(
                SystemBarStyler.SystemBarColor.BLACK,
                SystemBarStyler.SystemBarColor.BLACK,
            )
        } else {
            systemBarStyler.setStyle(SystemBarStyler.SystemBarColor.WHITE, SystemBarStyler.SystemBarColor.BLACK)
        }
    }

    private fun setFragmentResultListener() {
        setRecentCommentResultLisnter()
        setRecentPenaltyResultLisnter()
        setRecentSearchResultLisnter()
    }

    private fun setRecentCommentResultLisnter() {
        childFragmentManager.setFragmentResultListener(RecentCommentListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
            bundle.apply {
                val result = getInt(RecentCommentListFragment.ResultKey.WORD.name)
            }
        }
    }

    private fun setRecentPenaltyResultLisnter() {
        childFragmentManager.setFragmentResultListener(RecentPenaltyListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
            bundle.apply {
                val result = getInt(RecentPenaltyListFragment.ResultKey.PENALTY_ID.name)
            }
        }
    }

    private fun setRecentSearchResultLisnter() {
        childFragmentManager.setFragmentResultListener(RecentSearchListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
            navigateWithNavDirections(
                HomeFragmentDirections.actionHomeFragmentToSearchMedicinesFragment(
                    bundle.getString(
                        RecentSearchListFragment.ResultKey.WORD.name,
                    ) ?: "",
                ),
            )
        }
    }

    private fun setHeaderText() {
        val span: SpannableStringBuilder
        viewLifecycleOwner.apply {
            span = medicineInfoMapper.initHeaderSpan(requireContext(), getString(R.string.headerTextOnHome))
        }
        binding.headerText.text = span
    }
}
