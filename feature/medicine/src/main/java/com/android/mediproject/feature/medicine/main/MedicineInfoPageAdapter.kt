package com.android.mediproject.feature.medicine.main

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoFragment
import com.android.mediproject.feature.medicine.comments.HostCommentsFragment
import com.android.mediproject.feature.medicine.granule.GranuleInfoFragment
import com.android.mediproject.feature.medicine.precautions.host.MedicinePrecautionsFragment

class MedicineInfoPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicineBasicInfoFragment(), GranuleInfoFragment(), MedicinePrecautionsFragment(), HostCommentsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}