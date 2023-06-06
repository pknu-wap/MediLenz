package com.android.mediproject.feature.search.result.manual

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.mediproject.core.common.paging.setOnStateChangedListener
import com.android.mediproject.core.common.util.deepNavigate
import com.android.mediproject.core.model.constants.MedicationType
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.SearchMedicinesViewModel
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    private val searchMedicinesViewModel: SearchMedicinesViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel
            val searchResultListAdapter = ApprovedMedicinesAdapter().also {
                it.withLoadStateHeaderAndFooter(header = PagingLoadStateAdapter { it.retry() },
                    footer = PagingLoadStateAdapter { it.retry() })

                it.setOnStateChangedListener(pagingListViewGroup.messageTextView,
                    pagingListViewGroup.pagingList,
                    pagingListViewGroup.progressIndicator,
                    getString(R.string.searchResultIsEmpty))
            }

            pagingListViewGroup.pagingList.apply {
                setHasFixedSize(true)
                setItemViewCacheSize(10)
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    setDrawable(resources.getDrawable(com.android.mediproject.core.ui.R.drawable.divider, null))
                })
                adapter = searchResultListAdapter
            }

            filterBtn.setOnClickListener { it ->
                MediPopupMenu.showMenu(it, R.menu.search_result_list_filter_menu) { menuItem ->

                    when (menuItem.itemId) {
                        R.id.listOnlySpecialtyMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.SPECIALTY)

                        R.id.listOnlyGenericMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.GENERAL)

                        R.id.listAllMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.ALL)
                    }
                    true
                }
            }

            viewLifecycleOwner.repeatOnStarted {
                launch {
                    // 검색 결과를 수신하면 리스트에 표시한다.
                    fragmentViewModel.searchResultFlow.collectLatest {
                        searchResultListAdapter.submitData(it)
                    }
                }


                launch {
                    fragmentViewModel.eventState.collectLatest {
                        when (it) {
                            is EventState.OpenMedicineInfo -> {
                                openMedicineInfo(it.medicineInfoArgs)
                            }
                        }
                    }
                }
            }
        }

        fragmentViewModel.searchMedicinesByItemName(searchMedicinesViewModel.searchQuery.value)
    }

    private fun openMedicineInfo(medicineInfoArgs: MedicineInfoArgs) {
        activity?.findNavController(com.android.mediproject.core.common.R.id.fragmentContainerView)
            ?.deepNavigate("medilens://search/medicine/medicine_detail_nav", medicineInfoArgs)
    }
}