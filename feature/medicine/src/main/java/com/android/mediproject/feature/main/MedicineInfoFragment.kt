package com.android.mediproject.feature.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * 약 정보 화면
 *
 */
@AndroidEntryPoint
class MedicineInfoFragment : BaseFragment<FragmentMedicineInfoBinding, MedicineInfoViewModel>(FragmentMedicineInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.tabLayout.getTabAt(0)?.select()
    }

    // 탭 레이아웃 초기화
    private fun initTabs() {

        binding.apply {
            contentViewPager.adapter = MedicineInfoPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            // 탭 레이아웃에 탭 추가
            resources.getStringArray(R.array.medicineInfoTab).also { tabTextList ->
                TabLayoutMediator(tabLayout, contentViewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = tabTextList[0]
                        }

                        1 -> {
                            tab.text = tabTextList[1]
                        }

                        2 -> {
                            tab.text = tabTextList[2]
                        }

                        3 -> {
                            tab.text = tabTextList[3]
                        }
                    }
                }.attach()
            }
        }

    }

}