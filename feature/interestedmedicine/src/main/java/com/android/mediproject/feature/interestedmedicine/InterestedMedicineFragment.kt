package com.android.mediproject.feature.interestedmedicine

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.android.mediproject.core.model.medicine.InterestedMedicine.MedicineInterestedDto
import com.android.mediproject.core.ui.R
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
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnStarted {
                    fragmentViewModel.interstedMedicineList.collect { medicineList ->
                        setInterstedMedicineList(medicineList)
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
        binding.interstedMedicineHeaderView.apply {
            setOnExpandClickListener {

            }

            setOnMoreClickListener {
                findNavController().navigate("medilens://main/moreInterestedMedicine_nav".toUri())
            }
        }

    }

    /**
     * 마이페이지 즐겨찾기 목록 화면 로직
     */
    private fun setInterstedMedicineList(medicineList: List<MedicineInterestedDto>) {

        val horizontalSpace = resources.getDimension(com.android.mediproject.core.ui.R.dimen.dp_4).toInt()
        
        binding.interestedMedicineList.removeAllViews()

        //즐겨찾기 목록 약의 개수가 0개가 아닐 경우
        if (medicineList.size != 0) {
            medicineList.forEach { medicine ->
                log(medicine.toString())
                binding.interestedMedicineList.addView(
                    ButtonChip<Int>(
                        requireContext()
                    ).apply {
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
        } else {
            //0개 일 경우
            binding.apply {
                interestedMedicineList.visibility = View.GONE
                noInterstedMedicineTV.visibility = View.VISIBLE

                val span =
                    SpannableStringBuilder(getString(com.android.mediproject.feature.interestedmedicine.R.string.noInterstedMedicine)).apply {
                        setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.main
                                )
                            ), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        setSpan(
                            UnderlineSpan(),
                            0,
                            4,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.main
                                )
                            ), 6, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        setSpan(
                            UnderlineSpan(),
                            6,
                            8,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                    }
                noInterstedMedicineTV.text = span
                interstedMedicineHeaderView.setMoreVisiblity(false)
                interstedMedicineHeaderView.setExpandVisiblity(false)
            }
        }
    }
}