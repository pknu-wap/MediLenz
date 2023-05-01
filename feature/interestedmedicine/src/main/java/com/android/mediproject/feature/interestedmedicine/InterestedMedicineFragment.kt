package com.android.mediproject.feature.interestedmedicine

import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentInterestedMedicineBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestedMedicineFragment() : BaseFragment<FragmentInterestedMedicineBinding,InterstedMedicineViewModel>(
    FragmentInterestedMedicineBinding::inflate) {

    override val fragmentViewModel: InterstedMedicineViewModel by viewModels()


}