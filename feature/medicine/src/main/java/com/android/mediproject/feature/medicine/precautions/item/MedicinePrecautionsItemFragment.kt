package com.android.mediproject.feature.medicine.precautions.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicinePrecautionsItemBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import com.android.mediproject.feature.medicine.precautions.host.MedicinePrecautionsViewModel
import com.android.mediproject.feature.medicine.precautions.item.precautions.PrecautionsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.android.mediproject.core.common.viewmodel.repeatOnStarted

/**
 * 의약품 사용 상 주의사항 Fragment
 */
@AndroidEntryPoint
class MedicinePrecautionsItemFragment :
    BaseFragment<FragmentMedicinePrecautionsItemBinding, MedicineInfoViewModel>(FragmentMedicinePrecautionsItemBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    private val precautionsViewModel: MedicinePrecautionsViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            launch {
                fragmentViewModel.medicineDetails.collect {
                    if (it is UiState.Success) precautionsViewModel.createPrecautionsTexts(it.data.nbDocData)
                }
            }

            launch {
                precautionsViewModel.precautions.collect {
                    binding.medicinePrecautionsRecyclerview.adapter = PrecautionsListAdapter().apply {
                        submitList(it)
                    }
                }
            }
        }
    }


}
