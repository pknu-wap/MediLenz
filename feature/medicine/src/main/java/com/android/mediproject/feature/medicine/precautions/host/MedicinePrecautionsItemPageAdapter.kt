package com.android.mediproject.feature.medicine.precautions.host

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mediproject.feature.medicine.precautions.precautions.MedicinePrecautionsItemFragment
import com.android.mediproject.feature.medicine.precautions.dur.DurInfoFragment

class MedicinePrecautionsItemPageAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        MedicinePrecautionsItemFragment(), DurInfoFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}
