package com.android.mediproject.feature.medicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentVisibilityInfoBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 해당 화면은 의약품 식별 정보(낱알 정보, 형태 등)를 보여주는 화면입니다.
 *
 * 낱알 이미지, 형태(정제, 캡슐, 액제 등), 색상, 크기 등을 표시합니다.
 *
 * 상세 정보를 보기 위한 또는 버튼이 제공됩니다.
 */

@AndroidEntryPoint
class VisibilityInfoFragment : BaseFragment<FragmentVisibilityInfoBinding, MedicineInfoViewModel>(FragmentVisibilityInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }
}