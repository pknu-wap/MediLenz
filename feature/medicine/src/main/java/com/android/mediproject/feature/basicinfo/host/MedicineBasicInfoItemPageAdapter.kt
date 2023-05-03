package com.android.mediproject.feature.basicinfo.host

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.basicinfo.item.MedicineBasicInfoItemFragment

class MedicineBasicInfoItemPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int) = MedicineBasicInfoItemFragment()

}