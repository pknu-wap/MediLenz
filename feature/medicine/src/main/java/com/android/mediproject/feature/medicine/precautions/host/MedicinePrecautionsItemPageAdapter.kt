package com.android.mediproject.feature.medicine.precautions.host

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.precautions.item.MedicinePrecautionsItemFragment
import com.android.mediproject.feature.medicine.precautions.item.MedicineSafeUsageItemFragment

class MedicinePrecautionsItemPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicinePrecautionsItemFragment(), MedicineSafeUsageItemFragment()
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = fragments[position]

}