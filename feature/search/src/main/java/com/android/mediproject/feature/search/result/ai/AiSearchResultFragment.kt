package com.android.mediproject.feature.search.result.ai

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.deepNavigate
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.databinding.FragmentAiSearchResultBinding
import com.android.mediproject.feature.search.result.EventState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class AiSearchResultFragment :
    BaseFragment<FragmentAiSearchResultBinding, AiSearchResultViewModel>(FragmentAiSearchResultBinding::inflate) {

    override val fragmentViewModel: AiSearchResultViewModel by activityViewModels()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(logo, SystemBarStyler.SpacingType.MARGIN)))

            val searchResultListAdapter = AiResultAdapter()
            listViewGroup.messageTextView.text = getString(R.string.searchResultIsEmpty)

            val header = "<strong>${fragmentViewModel.classificationResult.value.size}</strong>${
                getString(com.android.mediproject.core.ui.R.string.detectioned_medicine_counts)
            }"
            detectionedCountsTextView.text = Html.fromHtml(header, Html.FROM_HTML_MODE_LEGACY)

            listViewGroup.pagingList.apply {
                setHasFixedSize(true)
                setItemViewCacheSize(10)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                        setDrawable(resources.getDrawable(com.android.mediproject.core.ui.R.drawable.divider, null))
                    },
                )
                adapter = searchResultListAdapter
            }

            viewLifecycleOwner.repeatOnStarted {
                launch {
                    fragmentViewModel.eventState.collectLatest {
                        when (it) {
                            is EventState.OpenMedicineInfo -> {
                                openMedicineInfo(it.medicineInfoArgs)
                            }
                        }
                    }
                }

                fragmentViewModel.medicineList.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            onChangedResult(state.data.isEmpty(), false)
                            searchResultListAdapter.submitList(state.data)
                        }

                        is UiState.Error -> {
                            onChangedResult(isEmpty = true, isLoading = false)
                        }

                        is UiState.Loading -> {
                            onChangedResult(isEmpty = true, isLoading = true)
                        }

                        is UiState.Initial -> {}
                    }
                }
            }
        }
    }

    private fun onChangedResult(isEmpty: Boolean, isLoading: Boolean) {
        binding.apply {
            listViewGroup.messageTextView.isVisible = isEmpty && !isLoading
            listViewGroup.pagingList.isVisible = !isEmpty || !isLoading
            listViewGroup.progressIndicator.isVisible = isLoading
        }
    }

    private fun openMedicineInfo(medicineInfoArgs: MedicineInfoArgs) {
        activity?.findNavController(com.android.mediproject.core.common.R.id.fragmentContainerView)
            ?.deepNavigate("medilens://search/medicine/medicine_detail_nav", medicineInfoArgs)
    }
}
