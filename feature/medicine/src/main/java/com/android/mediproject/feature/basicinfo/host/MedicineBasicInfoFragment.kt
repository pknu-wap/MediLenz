package com.android.mediproject.feature.basicinfo.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineBasicInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineBasicInfoFragment :
    BaseFragment<FragmentMedicineBasicInfoBinding, MedicineBasicInfoViewModel>(FragmentMedicineBasicInfoBinding::inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            medicineBasicInfoViewpager.adapter = MedicineBasicInfoItemPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            medicineBasicInfoChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                checkedIds.firstOrNull()?.let { checkedId ->
                    medicineBasicInfoViewpager.setCurrentItem(checkedId, true)
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.efficacyEffectChip.isChecked = true
    }
}