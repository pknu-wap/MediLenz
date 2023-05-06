package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.SearchMedicinesViewModel
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    private val searchMedicinesViewModel: SearchMedicinesViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchResultListAdapter = ApprovedMedicinesAdapter().also { adapter ->
            adapter.withLoadStateHeaderAndFooter(header = PagingLoadStateAdapter { adapter::retry },
                footer = PagingLoadStateAdapter { adapter::retry })
        }

        binding.apply {
            medicineSearchListLayout.manualSearchResultRecyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )

            medicineSearchListLayout.manualSearchResultRecyclerView.adapter = searchResultListAdapter

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    fragmentViewModel.searchResultFlow.collect {
                        searchResultListAdapter.submitData(lifecycle, it)
                    }
                }

                launch {
                    searchMedicinesViewModel.searchQuery.first().also { query ->
                        fragmentViewModel.searchMedicines(query)
                    }
                }
            }
        }
    }
}