package com.android.mediproject.feature.medicine.precautions.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicinePrecautionsItemBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import com.android.mediproject.feature.medicine.precautions.item.precautions.PrecautionsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

/**
 * 의약품 사용 상 주의사항 Fragment
 */
@AndroidEntryPoint
class MedicinePrecautionsItemFragment :
    BaseFragment<FragmentMedicinePrecautionsItemBinding, MedicineInfoViewModel>(FragmentMedicinePrecautionsItemBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.medicineDetails.collect {
                if (it is UiState.Success) addPrecautions(it.data.nbDocData)
            }
        }
    }

    private fun addPrecautions(xmlParsedResult: XMLParsedResult) {
        binding.apply {
            medicinePrecautionsRecyclerview.adapter = PrecautionsListAdapter().apply {
                submitList(xmlParsedResult.articleList)
            }
        }
    }

}