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
import kotlinx.coroutines.flow.flow
import repeatOnStarted

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseMedicineInfoItemFragment<T : ViewDataBinding, E : Any>(inflate: Inflate<T>) :
    BaseFragment<T, MedicineBasicInfoViewModel>(inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    protected val medicineInfoViewModel by viewModels<MedicineInfoViewModel>(ownerProducer = {
        requireParentFragment().let {
            it.requireParentFragment()
        }
    })

    companion object Instance {

        inline fun <reified T : BaseMedicineInfoItemFragment<*, *>> newInstance(basicInfoType: BasicInfoType): T {
            val fragment = T::class.java.newInstance()
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

    val collectingData: Flow<E> by lazy {
        flow {
            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.basicInfoType.collect { uiState ->
                    medicineInfoViewModel.getDetailInfo(uiState).let { detailInfo ->
                        detailInfo as E
                    }.apply {
                        emit(this)
                    }
                }
            }
        }
    }
}