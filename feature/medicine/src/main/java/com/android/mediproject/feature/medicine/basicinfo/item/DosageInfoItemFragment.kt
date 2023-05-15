package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoViewModel
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineBasicInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DosageInfoItemFragment :
    BaseFragment<FragmentMedicineBasicInfoItemBinding, MedicineBasicInfoViewModel>(FragmentMedicineBasicInfoItemBinding::inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}