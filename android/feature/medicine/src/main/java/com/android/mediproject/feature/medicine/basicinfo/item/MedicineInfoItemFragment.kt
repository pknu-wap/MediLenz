package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MedicineInfoItemFragment : BaseMedicineInfoItemFragment<FragmentMedicineInfoItemBinding>(
    FragmentMedicineInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.medicineInfo.collect {
                binding.contentsTextView.text = it
            }
        }

    }

}