package com.android.mediproject.feature.medicine.basicinfo.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineBasicInfoBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 의약품 기본정보 화면
 *
 * chip을 눌러서 효능효과, 용법용량, 의약품 정보를 볼 수 있다.
 */
@AndroidEntryPoint
class MedicineBasicInfoFragment :
    BaseFragment<FragmentMedicineBasicInfoBinding, MedicineBasicInfoViewModel>(FragmentMedicineBasicInfoBinding::inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            medicineBasicInfoViewpager.adapter = MedicineBasicInfoItemPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            medicineBasicInfoChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                medicineBasicInfoViewpager.setCurrentItem(group.indexOfChild(group.findViewById(checkedIds.first())), true)
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        binding.apply {
            medicineBasicInfoChipGroup.takeIf { it.checkedChipId == View.NO_ID }?.check(R.id.efficacyEffectChip)
        }
    }
}