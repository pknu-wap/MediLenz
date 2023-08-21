package com.android.mediproject.feature.search.result.ai

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.deepNavigate
import com.android.mediproject.core.common.viewmodel.onError
import com.android.mediproject.core.common.viewmodel.onInitial
import com.android.mediproject.core.common.viewmodel.onLoading
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.databinding.FragmentAiSearchResultBinding
import com.android.mediproject.feature.search.result.EventState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AiSearchResultFragment :
    BaseFragment<FragmentAiSearchResultBinding, AiSearchResultViewModel>(FragmentAiSearchResultBinding::inflate) {

    override val fragmentViewModel: AiSearchResultViewModel by viewModels()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(logo, SystemBarStyler.SpacingType.MARGIN)))

            val searchResultListAdapter = AiResultAdapter()
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
                fragmentViewModel.eventState.collectLatest {
                    when (it) {
                        is EventState.OpenMedicineInfo -> {
                            openMedicineInfo(it.medicineInfoArgs)
                        }
                    }
                }
            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.classificationResult.collect { state ->
                    state.onInitial { }.onLoading {

                    }.onSuccess { data ->

                    }.onError { }
                }
            }
        }
    }

    private fun openMedicineInfo(medicineInfoArgs: MedicineInfoArgs) {
        activity?.findNavController(com.android.mediproject.core.common.R.id.fragmentContainerView)
            ?.deepNavigate("medilens://search/medicine/medicine_detail_nav", medicineInfoArgs)
    }
}
