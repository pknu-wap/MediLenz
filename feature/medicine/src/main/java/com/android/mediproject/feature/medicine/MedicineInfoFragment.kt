package com.android.mediproject.feature.medicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineInfoFragment : BaseFragment<FragmentMedicineInfoBinding, MedicineInfoViewModel>(FragmentMedicineInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    // 탭 레이아웃 초기화
    private fun initTabs() {

        binding.apply {
            contentViewpager.adapter = MedicineInfoPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            
            resources.getStringArray(R.array.medicine_info_tab).also { tabTextList ->
                TabLayoutMediator(tabLayout, contentViewpager) { tab, position ->
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
                            tab.text = tabTextList[2]
                        }
                    }
                }.attach()
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabUnselected(tab: Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: Tab?) {
                TODO("Not yet implemented")
            }
        })
    }
}