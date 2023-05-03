package com.android.mediproject.feature.medicine

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.basicinfo.item.MedicineBasicInfoItemFragment

class MedicineInfoPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicineBasicInfoItemFragment(), MedicineBasicInfoItemFragment(), MedicineBasicInfoItemFragment(), MedicineBasicInfoItemFragment()
    )

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int) = fragments[position]

}