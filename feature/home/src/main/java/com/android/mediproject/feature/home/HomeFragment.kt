package com.android.mediproject.feature.home

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.util.SystemBarColorAnalyzer
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.ViewColorChanger
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.recentcommentlist.RecentCommentListFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import com.android.mediproject.feature.news.recents.RecentPenaltyListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {


    override val fragmentViewModel: HomeViewModel by viewModels()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    @Inject lateinit var systemBarColorAnalyzer: SystemBarColorAnalyzer

    @Inject lateinit var medicineInfoMapper: MedicineInfoMapper

    @Inject lateinit var viewColorChanger: ViewColorChanger

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
            is HomeViewModel.HomeEvent.NavigateToSearch -> findNavController().navigate(R.id.action_homeFragment_to_searchMedicinesFragment)
        }
    }

    private fun setBarStyle() {
        systemBarStyler.changeMode(
            listOf(
                SystemBarStyler.ChangeView(binding.homeBar, SystemBarStyler.SpacingType.MARGIN),
                SystemBarStyler.ChangeView(binding.headerLayout, SystemBarStyler.SpacingType.PADDING),
            ),
            emptyList(),
        )
        setBarScrollEffect()
    }

    private fun setBarScrollEffect() = binding.apply {
        root.doOnPreDraw {
            val divHeight = headerLayout.height - logoBackground.bottom
            var onWhite = false
            scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (onWhite != (scrollY >= divHeight)) {
                    onWhite = !onWhite

                    fragmentViewModel.setLogoColor(
                        if (onWhite) HomeViewModel.LogoColor.White else HomeViewModel.LogoColor.Main,
                    )

                    logoBackground.setBackgroundColor(
                        resources.getColor(
                            if (onWhite) com.android.mediproject.core.ui.R.color.white else com.android.mediproject.core.ui.R.color.main,
                            null,
                        ),
                    )
                    homeBar.setImageResource(
                        if (onWhite) com.android.mediproject.core.common.R.drawable.medilenz_original_logo else com.android.mediproject.core.common.R.drawable.medilenz_white_logo,
                    )

                    systemBarColorAnalyzer.convert()
                }

            }
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
            findNavController().navigate(
                R.id.action_homeFragment_to_searchMedicinesFragment,
                bundleOf("words" to bundle.getString(RecentSearchListFragment.ResultKey.WORD.name)),
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
