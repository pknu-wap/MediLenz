package com.android.mediproject.feature.medicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
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
        listOf(
            getString(R.string.default_info), getString(R.string.indentifying_info), getString(R.string.precautions), getString(
                R.string.comment
            )
        ).map {
            Tab().apply {
                text = it
                binding.tabLayout.addTab(this)
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