package com.android.mediproject.feature.interestedmedicine

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.interestedmedicine.databinding.FragmentInterestedMedicineBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestedMedicineFragment() :
    BaseFragment<FragmentInterestedMedicineBinding, InterstedMedicineViewModel>(
        FragmentInterestedMedicineBinding::inflate
    ) {

    override val fragmentViewModel: InterstedMedicineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addHistoryItemChips()
        initHeader()
    }

    /**
     * 최근 검색 목록 Chip을 추가합니다.
     *
     * 클릭 시 관련 로직을 수행하도록 합니다.
     */
    private fun addHistoryItemChips() {
        binding.apply {
            val horizontalSpace =
                resources.getDimension(com.android.mediproject.core.ui.R.dimen.dp_4).toInt()

            repeat(5) {
                this.interestedMedicineList.addView(ButtonChip<Int>(requireContext()).apply {
                    layoutParams = FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(horizontalSpace, 0, horizontalSpace, 0)
                    }

                    setChipText("타이레놀 $it")
                    setOnChipClickListener {
                        it?.apply { toast(this.toString()) }
                    }
                })
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