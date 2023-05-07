package com.android.mediproject.feature.main

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.basicinfo.host.MedicineBasicInfoFragment
import com.android.mediproject.feature.comments.commentsofamedicine.MedicineCommentsFragment
import com.android.mediproject.feature.precautions.host.MedicinePrecautionsFragment
import com.android.mediproject.feature.visibility.VisibilityInfoFragment

class MedicineInfoPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicineBasicInfoFragment(), VisibilityInfoFragment(), MedicinePrecautionsFragment(), MedicineCommentsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}