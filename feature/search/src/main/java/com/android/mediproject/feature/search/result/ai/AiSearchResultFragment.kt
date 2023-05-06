package com.android.mediproject.feature.search.result.ai

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.databinding.FragmentAiSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiSearchResultFragment :
    BaseFragment<FragmentAiSearchResultBinding, AiSearchResultViewModel>(FragmentAiSearchResultBinding::inflate) {

    override val fragmentViewModel: AiSearchResultViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

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