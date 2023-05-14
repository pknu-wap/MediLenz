package com.android.mediproject.feature.medicine.main

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoFragment
import com.android.mediproject.feature.comments.commentsofamedicine.MedicineCommentsFragment
import com.android.mediproject.feature.medicine.precautions.host.MedicinePrecautionsFragment
import com.android.mediproject.feature.medicine.visibility.VisibilityInfoFragment

class MedicineInfoPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicineBasicInfoFragment(), VisibilityInfoFragment(), MedicinePrecautionsFragment(), MedicineCommentsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}