package com.android.mediproject.feature.medicine.precautions.dur

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentDurInfoBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import com.android.mediproject.feature.medicine.precautions.dur.adapter.DurListAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * 의약품 적정 사용정보(DUR,Drug Utilization Review)
 * - 의약품의 효능, 용법, 용량, 주의사항 등을 알려주는 정보
 */
@AndroidEntryPoint
class DurInfoFragment : BaseFragment<FragmentDurInfoBinding, DurInfoViewModel>(FragmentDurInfoBinding::inflate) {

    override val fragmentViewModel: DurInfoViewModel by viewModels()

    private val medicineInfoViewModel by viewModels<MedicineInfoViewModel>(
        {
            requireParentFragment().requireParentFragment()
        },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = fragmentViewModel

        binding.apply {
            val adapter = DurListAdapter().apply {
                setOnStateChangedListener(
                    pagingListViewGroup.messageTextView,
                    pagingListViewGroup.loadTextView,
                    pagingListViewGroup.pagingList,
                    pagingListViewGroup.progressIndicator,
                    getString(com.android.mediproject.feature.medicine.R.string.dur_list_empty_message),
                    getString(com.android.mediproject.feature.medicine.R.string.dur_list_load_message),
                )
            }
            pagingListViewGroup.pagingList.adapter = adapter

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.durList.collect { state ->
                    state.isInitializing {

                    }.isLoadingDurTypes {

                    }.isLoadingDurList {

                    }.isSuccess {
                        log("isSuccess")
                        adapter.submitList(it)
                    }.isError {
                        log("isError")
                        adapter.submitList(emptyList())
                    }
                }
            }

            adapter.submitList(emptyList())
            fragmentViewModel.getDur(medicineInfoViewModel.medicinePrimaryInfo.replayCache.last().itemSeq.toString())
        }
    }

}
