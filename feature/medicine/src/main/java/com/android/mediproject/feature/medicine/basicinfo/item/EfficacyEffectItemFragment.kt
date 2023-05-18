package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import com.android.mediproject.feature.medicine.databinding.FragmentEfficacyInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

/**
 * 효능 효과 정보
 *
 */
@AndroidEntryPoint
class EfficacyEffectItemFragment : BaseMedicineInfoItemFragment<FragmentEfficacyInfoItemBinding>(
    FragmentEfficacyInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override

    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            medicineInfoViewModel.retrieveEfficacyEffect().collectLatest {
                binding.contentsTextView.text = it
            }
        }

    }

}