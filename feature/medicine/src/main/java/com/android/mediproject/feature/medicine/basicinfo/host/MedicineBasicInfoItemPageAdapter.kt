package com.android.mediproject.feature.medicine.basicinfo.host

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.basicinfo.item.DosageInfoItemFragment
import com.android.mediproject.feature.medicine.basicinfo.item.EfficacyEffectItemFragment
import com.android.mediproject.feature.medicine.basicinfo.item.MedicineInfoItemFragment
import com.android.mediproject.feature.medicine.main.BasicInfoType

class MedicineBasicInfoItemPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    private val infoTypes = listOf(
        BasicInfoType.EFFICACY_EFFECT, BasicInfoType.MEDICINE_INFO, BasicInfoType.DOSAGE
    )

    override fun createFragment(position: Int) = when (position) {
        0 -> EfficacyEffectItemFragment.newInstance(infoTypes[position])
        1 -> MedicineInfoItemFragment.newInstance(infoTypes[position])
        2 -> DosageInfoItemFragment.newInstance(infoTypes[position])
        else -> throw IllegalStateException("Invalid position $position")
    }

}