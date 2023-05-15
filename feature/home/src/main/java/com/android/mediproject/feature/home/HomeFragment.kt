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

        binding.apply{
            homeBar1.bringToFront()
            homeBar2.bringToFront()


            /**
             * 일반 적인 경우에는 emasredHeight로 그냥 측정되지만, 현재 headerLayout에 크기가 wrap_content이며,
             * headerLayout의 부모뷰인 ConstrainyLayout도 wrap_content라서 UNSPECIFED모드로 측정해주어야 합니다.
             * 다른 모드들은 전부 0으로 측정됩니다.
             */

            headerLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val initialHeight = (headerLayout.measuredHeight*1.2).toFloat()
            log(initialHeight.toString())

            scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                log(scrollY.toString())
                log((scrollY/initialHeight).toString())
                binding.homeBar2.alpha = (scrollY/initialHeight)
            }
        }
    }

    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnBarClickListener {
            Bundle().apply {
                putString("searchQuery", null)
                findNavController().navigate(R.id.action_homeFragment_to_searchMedicinesFragment, this)
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
                    getString(RecentSearchListFragment.ResultKey.WORD.name).also {
                        Bundle().apply {
                            putString("searchQuery", it)
                            findNavController().navigate(R.id.action_homeFragment_to_searchMedicinesFragment, this)
                        }
                    }

                }
            }
        }
    }
}