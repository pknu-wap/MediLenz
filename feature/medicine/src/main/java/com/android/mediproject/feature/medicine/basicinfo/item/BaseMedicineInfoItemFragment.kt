package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoViewModel
import com.android.mediproject.feature.medicine.main.BasicInfoType
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

interface MedicineInfoItemFragmentFactory {
    fun <T : BaseMedicineInfoItemFragment<*, *>> newInstance(basicInfoType: BasicInfoType, fragmentClass: KClass<T>): T
}

abstract class BaseMedicineInfoItemFragment<T : ViewDataBinding, E : Any>(inflate: Inflate<T>) :
    BaseFragment<T, MedicineBasicInfoViewModel>(inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    private val _medicineInfoViewModel by viewModels<MedicineInfoViewModel>(ownerProducer = {
        requireParentFragment().requireParentFragment()
    })

    protected val medicineInfoViewModel get() = _medicineInfoViewModel


    companion object : MedicineInfoItemFragmentFactory {

        @JvmStatic
        override fun <T : BaseMedicineInfoItemFragment<*, *>> newInstance(
            basicInfoType: BasicInfoType, fragmentClass: KClass<T>
        ): T {
            val fragment = fragmentClass.createInstance()
            fragment.arguments = Bundle().apply {
                putParcelable("basicInfoType", basicInfoType)
            }
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("basicInfoType", BasicInfoType::class.java)
            } else {
                getParcelable("basicInfoType") as BasicInfoType?
            }?.let { basicInfoType ->
                fragmentViewModel.setBasicInfoType(basicInfoType)
            }
        }

    }

    protected val collectingData: Flow<E> by lazy {
        channelFlow<E> {
            fragmentViewModel.basicInfoType.collectLatest { type ->
                _medicineInfoViewModel.getDetailInfo(type).let { detailInfo ->
                    detailInfo as E
                }.apply {
                    send(this)
                }
            }
        }
    }


}