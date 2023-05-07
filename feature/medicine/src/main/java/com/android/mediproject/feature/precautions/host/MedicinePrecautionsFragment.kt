package com.android.mediproject.feature.precautions.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
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
                checkedIds.firstOrNull()?.let { checkedId ->
                    precautionsViewpager.setCurrentItem(checkedId, true)
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.precautionsChip.isChecked = true
    }
}