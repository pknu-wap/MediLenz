package com.android.mediproject.feature.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.MediSearchbar
import com.android.mediproject.feature.search.databinding.FragmentSearchMedicinesBinding
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class SearchMedicinesFragment :
    BaseFragment<FragmentSearchMedicinesBinding, SearchMedicinesViewModel>(FragmentSearchMedicinesBinding::inflate) {

    override val fragmentViewModel by viewModels<SearchMedicinesViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // contents_fragment_container_view 에 최근 검색 목록과 검색 결과 목록 화면 두 개를 띄운다.
        initSearchBar()

        arguments?.apply {
            getString("searchQuery")?.also {
                binding.searchView.searchWithQuery(query = it)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.searchQuery.collect { query ->
                if (query.isNotBlank()) {
                    binding.contentsFragmentContainerView.findNavController().also { navController ->
                        navController.currentDestination?.also { currentDestination ->
                            if (currentDestination.id == R.id.manualSearchResultFragment) navController.navigate(R.id.action_manualSearchResultFragment_self)
                            else navController.navigate(
                                RecentSearchListFragmentDirections.actionRecentSearchListFragmentToManualSearchResultFragment()
                            )

                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            childFragmentManager.findFragmentById(R.id.contentsFragmentContainerView)?.also { navHostFragment ->
                navHostFragment.childFragmentManager.apply {
                    setFragmentResultListener(
                        RecentSearchListFragment.ResultKey.RESULT_KEY.name, navHostFragment.viewLifecycleOwner
                    ) { _, bundle ->

                        bundle.apply {
                            getString(RecentSearchListFragment.ResultKey.WORD.name)?.also {
                                binding.searchView.searchWithQuery(query = it)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnSearchAiBtnClickListener {}

        binding.searchView.setOnSearchBtnClickListener(object : MediSearchbar.SearchQueryCallback {
            override fun onSearchQuery(query: String) {
                fragmentViewModel.searchMedicines(query)
            }

            override fun onEmptyQuery() {
                toast(getString(com.android.mediproject.core.ui.R.string.search_empty_query))
            }
        })
    }
}