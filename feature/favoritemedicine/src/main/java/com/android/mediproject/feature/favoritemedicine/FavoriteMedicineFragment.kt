package com.android.mediproject.feature.favoritemedicine

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
import androidx.navigation.findNavController
import com.android.mediproject.core.model.interestedmedicine.InterestedMedicineDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentFavoriteMedicineBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class FavoriteMedicineFragment :
    BaseFragment<FragmentFavoriteMedicineBinding, FavoriteMedicineViewModel>(
        FragmentFavoriteMedicineBinding::inflate
    ) {

    override val fragmentViewModel: FavoriteMedicineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFavoriteMedicinesChips()
        initHeader()
    }

    private fun addFavoriteMedicinesChips() {
        binding.apply {
            fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted {
                        favoriteMedicineList.collect {
                            setInterstedMedicineList(it)
                        }
                    }
                    repeatOnStarted {
                        token.collect {
                            handleToken(it)
                        }
                    }
                }
                loadTokens()
            }
        }
    }

    private fun handleToken(token: TokenState<CurrentTokenDto>) {
        when (token) {
            is TokenState.Empty -> {}
            is TokenState.Error -> {}
            is TokenState.RefreshExpiration -> {}
            is TokenState.AccessExpiration -> {}
            is TokenState.Valid -> {
                fragmentViewModel.loadFavoriteMedicines()
            }
        }
    }

    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.favoriteMedicineHeaderView.apply {
            setOnMoreClickListener {
                findNavController().navigate("medilens://main/moreInterestedMedicine_nav".toUri())
            }
        }

    }

    /**
     * 마이페이지 즐겨찾기 목록 화면 로직
     */
    private fun setInterstedMedicineList(medicineList: List<InterestedMedicineDto>) {
        //다른화면 갔다올 경우 이전에 있는 약품에 더해서 더 생기기 때문에 제거해줘야 함
        binding.favoriteMedicineList.removeAllViews()

        //즐겨찾기 목록 약의 개수가 0개가 아닐 경우
        if (medicineList.size != 0) {

            val horizontalSpace = resources.getDimension(R.dimen.dp_4).toInt()

            medicineList.forEach { medicine ->
                log(medicine.toString())
                binding.favoriteMedicineList.addView(
                    ButtonChip<String>(
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
            showNoInterestedMedicine()
        }
    }

    private fun showNoInterestedMedicine() {
        log("즐겨찾기 없음")
        binding.apply {
            favoriteMedicineList.visibility = View.GONE
            noFavoriteMedicineTV.visibility = View.VISIBLE

            val span =
                SpannableStringBuilder(getString(com.android.mediproject.feature.interestedmedicine.R.string.noFavoriteMedicine)).apply {
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
            noFavoriteMedicineTV.text = span
            favoriteMedicineHeaderView.setMoreVisiblity(false)
            favoriteMedicineHeaderView.setExpandVisiblity(false)
        }
    }
}