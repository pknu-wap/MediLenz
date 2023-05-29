package com.android.mediproject.feature.search

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.uiutil.hideKeyboard
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

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.searchQuery.collect { query ->
                // Flow로 받은 문자열이 일치하는 경우에만 searchView에 표시한다.
                if (!binding.searchView.getText().contentEquals(query)) binding.searchView.setText(query)
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
                RecentSearchListFragmentDirections.actionRecentSearchListFragmentToManualSearchResultFragment()
            )
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        childFragmentManager.findFragmentById(R.id.contentsFragmentContainerView)?.also { navHostFragment ->
            val childFragmentManager = navHostFragment.childFragmentManager
            childFragmentManager.setFragmentResultListener(
                RecentSearchListFragment.ResultKey.RESULT_KEY.name, navHostFragment.viewLifecycleOwner
            ) { _, bundle ->
                bundle.getString(RecentSearchListFragment.ResultKey.WORD.name)?.also {
                    binding.searchView.searchWithQuery(it)
                }
            }
        }

        // 검색어가 전달된 경우에는 검색어를 넣은 후 검색
        arguments?.getString("query")?.takeIf {
            it.isNotEmpty()
        }?.also {
            binding.searchView.setText(it)
            binding.contentsFragmentContainerView.findNavController().navigate(
                RecentSearchListFragmentDirections.actionRecentSearchListFragmentToManualSearchResultFragment(),
                NavOptions.Builder().setPopUpTo(R.id.manualSearchResultFragment, true).build()
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

        binding.searchView.setOnSearchBtnClickListener(object : MediSearchbar.SearchQueryCallback {
            override fun onSearchQuery(query: String) {
                hideKeyboard()
                fragmentViewModel.setQuery(query)
                moveToManualSearchResultFragment()
            }

            override fun onEmptyQuery() {
                toast(getString(com.android.mediproject.core.ui.R.string.search_empty_query))
            }
        })
    }
}