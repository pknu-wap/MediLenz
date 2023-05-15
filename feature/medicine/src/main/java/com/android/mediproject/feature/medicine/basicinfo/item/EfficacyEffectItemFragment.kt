package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoViewModel
import com.android.mediproject.feature.medicine.databinding.FragmentEfficacyInfoItemBinding
import com.android.mediproject.feature.medicine.main.BasicInfoType
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class EfficacyEffectItemFragment :
    BaseFragment<FragmentEfficacyInfoItemBinding, MedicineBasicInfoViewModel>(FragmentEfficacyInfoItemBinding::inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    private val medicineInfoViewModel by viewModels<MedicineInfoViewModel>(ownerProducer = {
        requireParentFragment().let {
            it.requireParentFragment()
        }
    })

    companion object {
        fun newInstance(basicInfoType: BasicInfoType): EfficacyEffectItemFragment = EfficacyEffectItemFragment().apply {
            arguments = Bundle().apply {
                putParcelable("basicInfoType", basicInfoType)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.basicInfoType.collectLatest { type ->
                medicineInfoViewModel.getDetailInfo(type).let { detailInfo ->
                    detailInfo as XMLParsedResult
                }.apply {
                    binding.contentsTextView.text = this.title
                }
            }
        }

        arguments?.apply {
            this.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable("basicInfoType", BasicInfoType::class.java)
                } else {
                    it.getParcelable("basicInfoType") as BasicInfoType?
                }?.let { basicInfoType ->
                    fragmentViewModel.setBasicInfoType(basicInfoType)
                }
            }
        }


    }

}