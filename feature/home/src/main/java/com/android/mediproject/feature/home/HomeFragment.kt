package com.android.mediproject.feature.home

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.mapper.MedicineInfoMapper
import com.android.mediproject.core.common.util.SystemBarColorAnalyzer
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
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

    @Inject lateinit var systemBarStyler: SystemBarController

    @Inject lateinit var systemBarColorAnalyzer: SystemBarColorAnalyzer

    @Inject lateinit var medicineInfoMapper: MedicineInfoMapper


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
                SystemBarStyler.ChangeView(binding.logoBackground, SystemBarStyler.SpacingType.PADDING),
                SystemBarStyler.ChangeView(binding.headerLayout, SystemBarStyler.SpacingType.PADDING),
            ),
        )
        setBarScrollEffect()
    }

    private fun setBarScrollEffect() = binding.apply {
        root.doOnPreDraw {
            ViewState.init(requireContext())
            val divHeight = headerLayout.height - logoBackground.bottom
            var lastState: ViewState = ViewState.Default

            scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val newState = if (scrollY >= divHeight) ViewState.OnWhite else ViewState.Default
                if (lastState != newState) {
                    val backgroundColorChange = ObjectAnimator.ofInt(
                        logoBackground, PROPERTY_BACKGROUND_COLOR,
                        lastState.color,
                        newState.color,
                    ).apply {
                        setEvaluator(ArgbEvaluator())
                        duration = ANIM_DURATION
                    }

                    val fadeOutAnim = ObjectAnimator.ofFloat(homeBar, PROPERTY_ALPHA, 1f, 0f).apply {
                        duration = FADE_INT_OUT_DURATION

                        val fadeInAnim = ObjectAnimator.ofFloat(homeBar, PROPERTY_ALPHA, 0f, 1f).apply {
                            duration = FADE_INT_OUT_DURATION
                            doOnEnd {
                                systemBarColorAnalyzer.convert()
                            }
                        }

                        doOnEnd {
                            // logoChange
                            kotlinx.coroutines.Runnable {
                                homeBar.setImageDrawable(
                                    newState.drawable,
                                )
                            }.run()
                            fadeInAnim.start()
                        }
                    }

                    val anims = AnimatorSet()
                    anims.playTogether(backgroundColorChange, fadeOutAnim)
                    anims.start()

                    lastState = newState
                }

            }
        }
    }

    private fun setFragmentResultListener() {
        setRecentCommentResultListener()
        setRecentPenaltyResultListener()
        setRecentSearchResultListener()
    }

    private fun setRecentCommentResultListener() {
        childFragmentManager.setFragmentResultListener(RecentCommentListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
            bundle.apply {
                val result = getInt(RecentCommentListFragment.ResultKey.WORD.name)
            }
        }
    }

    private fun setRecentPenaltyResultListener() {
        childFragmentManager.setFragmentResultListener(RecentPenaltyListFragment.ResultKey.RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
            bundle.apply {
                val result = getInt(RecentPenaltyListFragment.ResultKey.PENALTY_ID.name)
            }
        }
    }

    private fun setRecentSearchResultListener() {
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

    private sealed class ViewState {
        abstract val drawable: Drawable
        abstract val color: Int

        companion object {
            protected var drawables = mapOf<ViewState, Drawable>()
            protected var colors = mapOf<ViewState, Int>()

            @SuppressLint("UseCompatLoadingForDrawables")
            fun init(context: Context) {
                drawables = mapOf(
                    ViewState.Default to context.resources.getDrawable(com.android.mediproject.core.common.R.drawable.medilenz_white_logo, null),
                    ViewState.OnWhite to context.resources.getDrawable(com.android.mediproject.core.common.R.drawable.medilenz_original_logo, null),
                )
                colors = mapOf(
                    ViewState.Default to context.resources.getColor(com.android.mediproject.core.ui.R.color.main, null),
                    ViewState.OnWhite to context.resources.getColor(com.android.mediproject.core.ui.R.color.white, null),
                )
            }
        }

        object Default : ViewState() {
            override val drawable: Drawable
                get() = drawables[ViewState.Default]!!
            override val color: Int
                get() = colors[ViewState.Default]!!
        }

        object OnWhite : ViewState() {
            override val drawable: Drawable
                get() = drawables[ViewState.OnWhite]!!
            override val color: Int
                get() = colors[ViewState.OnWhite]!!
        }
    }

    private companion object ViewAnimConsts {
        const val ANIM_DURATION = 250L
        const val FADE_INT_OUT_DURATION = 150L
        const val PROPERTY_BACKGROUND_COLOR = "backgroundColor"
        const val PROPERTY_ALPHA = "alpha"
    }
}
