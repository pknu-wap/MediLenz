package com.android.mediproject.feature.medicine.precautions.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicinePrecautionsHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicinePrecautionsFragment :
    BaseFragment<FragmentMedicinePrecautionsHostBinding, MedicinePrecautionsViewModel>(FragmentMedicinePrecautionsHostBinding::inflate) {

    override val fragmentViewModel: MedicinePrecautionsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            precautionsViewpager.adapter = MedicinePrecautionsItemPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            
            chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                precautionsViewpager.setCurrentItem(group.indexOfChild(group.findViewById(checkedIds.first())), true)
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        binding.apply {
            chipGroup.takeIf { it.checkedChipId == View.NO_ID }?.check(R.id.precautionsChip)
        }
    }
}
