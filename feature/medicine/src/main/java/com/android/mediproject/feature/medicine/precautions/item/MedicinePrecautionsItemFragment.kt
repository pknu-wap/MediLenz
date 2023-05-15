package com.android.mediproject.feature.medicine.precautions.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicinePrecautionsItemBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class MedicinePrecautionsItemFragment :
    BaseFragment<FragmentMedicinePrecautionsItemBinding, MedicineInfoViewModel>(FragmentMedicinePrecautionsItemBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.medicineDetails.collectLatest {
            }
        }
    }

}