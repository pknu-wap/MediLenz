package com.android.mediproject.feature.medicine.basicinfo.host

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.basicinfo.item.DosageInfoItemFragment
import com.android.mediproject.feature.medicine.basicinfo.item.EfficacyEffectItemFragment
import com.android.mediproject.feature.medicine.basicinfo.item.MedicineInfoItemFragment

class MedicineBasicInfoItemPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int) = when (position) {
        0 -> EfficacyEffectItemFragment.newInstance(EfficacyEffectItemFragment::class)
        1 -> MedicineInfoItemFragment.newInstance(MedicineInfoItemFragment::class)
        2 -> DosageInfoItemFragment.newInstance(DosageInfoItemFragment::class)
        else -> throw IllegalStateException("Invalid position $position")
    }

}