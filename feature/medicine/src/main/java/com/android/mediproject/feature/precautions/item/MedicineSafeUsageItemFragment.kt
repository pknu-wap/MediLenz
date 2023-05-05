package com.android.mediproject.feature.precautions.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineSafeUseItemBinding
import com.android.mediproject.feature.precautions.host.MedicinePrecautionsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * 의약품 적정 사용정보(DUR,Drug Utilization Review)
 *
 * 의약품 적정사용 정보는 부적절한 의약품 사용을 사전에 예방하고 국민건강을 보호하기 위하여,
 * 의·약 전문가가 처방·조제 시 활용할 수 있도록 의약품의 안전하고 적절한 사용 기준을 항목별로 분류하여 체계적으로 개발한 정보입니다.
 *
 *  품목기준코드 또는 품목명으로 조회 가능
 *
 *  병용금기	◎ 두 가지 이상의 의약품을 함께 사용할 때 치료효과의 변화 또는 심각한 부작용 발생 등의 우려가 있어 동시에 사용하지 않아야 하는 의약품의 조합
 *
 *  특정연령대금기	◎ 소아, 노인 등 특정한 연령대의 환자가 사용함에 있어 안전성이 확보되지 않았거나 심각한 부작용 발생 등의 우려가 있어 사용하지 않아야 하는 의약품
 *
 *  임부금기	◎ 태아에게 매우 심각한 위해성(태아기형 또는 태아독성 등)을 유발하거나 유발할 가능성이 높아 임부에게 사용하는 것이 권장되지 않는 의약품
 *
 *  1등급 : 태아에 대한 위험성이 명확하여 의학적으로 불가피한 경우 이외에는 반드시 임부에게 투여를 피해야하는 의약품
 *
 *  2등급 : 태아에 대한 위해성이 나타날 수 있으므로 원칙적으로 사용금지. 다만, 임부에 대한 치료적 유익성이 위험성보다 더 높을 경우 신중하게 투여 가능한 의약품
 *
 *  용량주의	◎ 성인에서 특정용량을 초과하여 투여 시 효과의 증가는 기대하기 어렵고 용량 의존적 부작용 발생 가능성이 높아져 1일 최대용량에 대한 주의가 필요한 의약품
 *
 *  투여기간주의	◎ 특정 투여기간을 초과하여 투여 시 효과의 증가는 기대하기 어렵고 부작용 발생 가능성이 높아져 1회 최대 투여기간에 대한 주의가 필요한 의약품
 *
 *  노인주의	◎ 노인에서 부작용 발생 빈도 증가 등의 우려가 있어 사용 시 주의가 필요한 의약품
 *
 *  효능군중복	◎ 약리기전이 동일하거나 유사한 효능군 내에서 중복 투여될 때 추가적인 효과의 증가는 기대하기 어렵고 부작용 발생 가능성이 높아져 주의가 필요한 의약품
 *
 *  분할주의	◎ 분할하여 복용할 경우 허가된 약효를 기대하기 어려운 의약품
 *
 *  첨가제주의	◎ 특정 첨가제 성분(유당, 대두유, 카제인)의 주의가 필요한 의약품
 *
 */
@AndroidEntryPoint
class MedicineSafeUsageItemFragment :
    BaseFragment<FragmentMedicineSafeUseItemBinding, MedicinePrecautionsViewModel>(FragmentMedicineSafeUseItemBinding::inflate) {

    override val fragmentViewModel: MedicinePrecautionsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}