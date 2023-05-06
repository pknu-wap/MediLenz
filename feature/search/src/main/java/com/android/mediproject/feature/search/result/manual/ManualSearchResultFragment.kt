package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.UiState
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

    private val searchMedicinesViewModel by viewModels<SearchMedicinesViewModel>({ requireParentFragment() })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val searchResultListAdapter = ApprovedMedicinesAdapter().also { adapter ->
                adapter.withLoadStateHeaderAndFooter(header = PagingLoadStateAdapter { adapter::retry },
                    footer = PagingLoadStateAdapter { adapter::retry })
            }
            medicineSearchListLayout.manualSearchResultRecyclerView.adapter = searchResultListAdapter


            viewLifecycleOwner.repeatOnStarted {
                searchMedicinesViewModel.searchResult.collect { state ->
                    when (state) {
                        is UiState.isInitial -> {
                        }

                        is UiState.isLoading -> {
                        }

                        is UiState.Success -> {
                            state.data.collectLatest {
                                searchResultListAdapter.submitData(it)
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
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