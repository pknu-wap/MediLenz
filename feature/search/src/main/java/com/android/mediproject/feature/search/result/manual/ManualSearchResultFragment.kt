package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()

    private val searchResultListAdapter = SearchResultListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // medicineSearchListLayout.manualSearchResultRecyclerView.adapter = searchResultListAdapter

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

class SearchResultListAdapter : RecyclerView.Adapter<SearchResultListAdapter.SearchResultViewHolder>() {

    /*
    private val asyncDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<SearchResultItem>() {
        override fun areItemsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
            return oldItem == newItem
        }
    })

     */

    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        TODO()
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        TODO()
    }

    override fun getItemCount(): Int {
        TODO()
    }


}