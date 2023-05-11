package com.android.mediproject.feature.interestedmedicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentMoreInterestedMedicineBinding

class MoreInterestedMedicineFragment : BaseFragment<FragmentMoreInterestedMedicineBinding,MoreInterestedMedicineViewModel>(FragmentMoreInterestedMedicineBinding::inflate) {
    override val fragmentViewModel: MoreInterestedMedicineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = fragmentViewModel.apply{

            }
        }
    }

}