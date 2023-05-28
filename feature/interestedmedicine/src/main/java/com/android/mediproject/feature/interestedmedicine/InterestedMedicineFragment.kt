package com.android.mediproject.feature.interestedmedicine

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.mediproject.core.model.medicine.InterestedMedicine.MedicineInterestedDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentInterestedMedicineBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class InterestedMedicineFragment() :
    BaseFragment<FragmentInterestedMedicineBinding, InterstedMedicineViewModel>(
        FragmentInterestedMedicineBinding::inflate
    ) {

    override val fragmentViewModel: InterstedMedicineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addInterestedMedicinesChips()
        initHeader()
    }

    private fun addInterestedMedicinesChips() {
        val horizontalSpace =
            resources.getDimension(com.android.mediproject.core.ui.R.dimen.dp_4).toInt()
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnStarted {
                    fragmentViewModel.medicineIntersted.collect { medicineList ->
                        medicineList.forEach { medicine ->
                            log(medicine.toString())
                            this@apply.interestedMedicineList.addView(ButtonChip<Int>(requireContext()).apply {
                                layoutParams = FlexboxLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(horizontalSpace, 0, horizontalSpace, 0)
                                }
                                setChipText(medicine.medicineName)
                                data = medicine.itemSeq
                                setOnChipClickListener {
                                    toast(it.toString())
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.headerView.apply {
            setOnExpandClickListener {
            }
            setOnMoreClickListener {
            }
        }

    }
}