package com.android.mediproject.feature.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.MediSearchbar
import com.android.mediproject.feature.search.databinding.FragmentSearchMedicinesHostBinding
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMedicinesFragment :
    BaseFragment<FragmentSearchMedicinesHostBinding, SearchMedicinesViewModel>(FragmentSearchMedicinesHostBinding::inflate) {

    override val fragmentViewModel: SearchMedicinesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // contents_fragment_container_view 에 최근 검색 목록과 검색 결과 목록 화면 두 개를 띄운다.
        initSearchBar()

        childFragmentManager.apply {
            setFragmentResultListener(RecentSearchListFragment.ResultKey.RESULT_KEY.name) { _, bundle ->

                bundle.apply {
                    val result = getInt(RecentSearchListFragment.ResultKey.WORD.name)
                    findNavController().navigate(SearchMedicinesFragmentDirections.actionSearchMedicinesFragmentToManualSearchResultNav())
                }
            }
        }
    }

    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnSearchAiBtnClickListener {
            toast("AI검색을 초기화합니다")
        }

        binding.searchView.setOnSearchBtnClickListener(object : MediSearchbar.SearchQueryCallback {
            override fun onSearchQuery(query: String) {
                toast("$query 를 검색합니다")
                findNavController().navigate(SearchMedicinesFragmentDirections.actionSearchMedicinesFragmentToManualSearchResultNav())
            }

            override fun onEmptyQuery() {
                toast(getString(com.android.mediproject.core.ui.R.string.search_empty_query))
            }
        })
    }
}