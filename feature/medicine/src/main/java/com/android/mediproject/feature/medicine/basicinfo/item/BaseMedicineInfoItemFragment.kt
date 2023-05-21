package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoViewModel
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

interface MedicineInfoItemFragmentFactory {
    fun <T : BaseMedicineInfoItemFragment<*>> newInstance(fragmentClass: KClass<T>): T
}

abstract class BaseMedicineInfoItemFragment<T : ViewDataBinding>(inflate: Inflate<T>) :
    BaseFragment<T, MedicineBasicInfoViewModel>(inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val _medicineInfoViewModel by viewModels<MedicineInfoViewModel>(ownerProducer = {
        requireParentFragment().requireParentFragment()
    })

    protected val medicineInfoViewModel get() = _medicineInfoViewModel


    companion object : MedicineInfoItemFragmentFactory {

        @JvmStatic
        override fun <T : BaseMedicineInfoItemFragment<*>> newInstance(
            fragmentClass: KClass<T>
        ): T {
            return fragmentClass.createInstance()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            medicineInfoViewModel.medicineDetails.collectLatest {
                fragmentViewModel.setMedicineDetails(it)
            }
        }
    }

}