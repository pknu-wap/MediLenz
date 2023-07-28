package com.android.mediproject.feature.search

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.util.SystemBarColorAnalyzer
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.hideKeyboard
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.MediSearchbar
import com.android.mediproject.feature.search.databinding.FragmentSearchMedicinesBinding
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class SearchMedicinesFragment : BaseFragment<FragmentSearchMedicinesBinding, SearchMedicinesViewModel>(FragmentSearchMedicinesBinding::inflate) {

    @Inject lateinit var systemBarStyler: SystemBarController
    @Inject lateinit var systemBarColorAnalyzer: SystemBarColorAnalyzer

    override val fragmentViewModel by viewModels<SearchMedicinesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(binding.root, SystemBarStyler.SpacingType.PADDING)))

        initSearchBar()
        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.searchQuery.collectLatest { query ->
                // Flow로 받은 문자열이 일치하는 경우에만 searchView에 표시한다.
                if (!binding.searchView.getText().contentEquals(query)) binding.searchView.setText(query)
            }
        }

        // 검색어가 전달된 경우에는 검색어를 넣은 후 검색
        arguments?.getString("words")?.takeIf {
            it.isNotEmpty()
        }?.also {
            childFragmentManager.findFragmentById(binding.contentsFragmentContainerView.id)?.apply {
                val navController = (this as NavHostFragment).findNavController()

                if (navController.currentDestination?.id != R.id.manualSearchResultFragment) {
                    fragmentViewModel.setQuery(it)
                    navController.navigate(
                        RecentSearchListFragmentDirections.actionRecentSearchListFragmentToManualSearchResultFragment(),
                        NavOptions.Builder().setPopUpTo(R.id.manualSearchResultFragment, true).build(),
                    )
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        childFragmentManager.findFragmentById(R.id.contentsFragmentContainerView)?.also { navHostFragment ->
            val childFragmentManager = navHostFragment.childFragmentManager
            childFragmentManager.setFragmentResultListener(
                RecentSearchListFragment.ResultKey.RESULT_KEY.name,
                navHostFragment.viewLifecycleOwner,
            ) { _, bundle ->
                bundle.getString(RecentSearchListFragment.ResultKey.WORD.name)?.also {
                    binding.searchView.searchWithQuery(it)
                }
            }
        }
    }


    private fun moveToManualSearchResultFragment() {
        val navController = binding.contentsFragmentContainerView.findNavController()
        navController.currentDestination?.also { currentDestination ->
            // 현재 프래그먼트가 검색 결과 프래그먼트인 경우에는 백스택에서 검색 결과 프래그먼트를 제거하고 검색 결과 프래그먼트로
            // 다시 이동한다.
            // 검색 결과 프래그먼트가 아닌 경우에는 검색 결과 프래그먼트로 이동한다.
            if (currentDestination.id == R.id.manualSearchResultFragment) navController.navigate(R.id.action_manualSearchResultFragment_self)
            else navController.navigate(
                RecentSearchListFragmentDirections.actionRecentSearchListFragmentToManualSearchResultFragment(),
            )
        }
    }


    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnSearchAiBtnClickListener {
            findNavController().navigate("medilens://main/camera_nav".toUri())
        }

        binding.searchView.setOnSearchBtnClickListener(
            object : MediSearchbar.SearchQueryCallback {
                override fun onSearchQuery(query: String) {
                    hideKeyboard()
                    fragmentViewModel.setQuery(query)
                    moveToManualSearchResultFragment()
                }

                override fun onEmptyQuery() {
                    toast(getString(com.android.mediproject.core.ui.R.string.search_empty_query))
                }
            },
        )
    }
}
