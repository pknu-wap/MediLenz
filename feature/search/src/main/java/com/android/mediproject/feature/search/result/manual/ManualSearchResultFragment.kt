package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.SearchMedicinesViewModel
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()

    private val searchMedicinesViewModel by viewModels<SearchMedicinesViewModel>({ requireParentFragment().requireParentFragment() })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val searchResultListAdapter = ApprovedMedicinesAdapter().also { adapter ->
                adapter.withLoadStateHeaderAndFooter(header = PagingLoadStateAdapter { adapter::retry },
                    footer = PagingLoadStateAdapter { adapter::retry })
            }

            medicineSearchListLayout.manualSearchResultRecyclerView.adapter = searchResultListAdapter

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.searchResultFlow.collectLatest {
                    searchResultListAdapter.submitData(lifecycle, it)
                }

                searchMedicinesViewModel.searchQuery.collectLatest {
                    fragmentViewModel.searchMedicines(it)
                }
            }

            medicineSearchListLayout.filterButton.setOnClickListener { it ->
                MediPopupMenu.showMenu(
                    it, R.menu.search_result_list_filter_menu
                ) { menuItem ->
                    when (menuItem.itemId) {
                        R.id.option_show_only_specialty_medicines -> {
                        }

                        R.id.option_show_only_generic_medicines -> {
                        }

                        R.id.option_show_all_medicines -> {
                        }
                    }
                    true
                }
            }
        }
    }
}