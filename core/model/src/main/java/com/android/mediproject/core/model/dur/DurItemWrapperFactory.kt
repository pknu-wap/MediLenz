package com.android.mediproject.core.model.dur

import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.dur.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.capacity.DurProductCapacityAttentionWrapper
import com.android.mediproject.core.model.dur.durproduct.combination.DurProductCombinationTabooResponse
import com.android.mediproject.core.model.dur.durproduct.combination.DurProductCombinationTabooWrapper
import com.android.mediproject.core.model.dur.durproduct.dosing.DurProductDosingCautionResponse
import com.android.mediproject.core.model.dur.durproduct.dosing.DurProductDosingCautionWrapper
import com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationResponse
import com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationWrapper
import com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet.DurProductExReleaseTableSplitAttentionResponse
import com.android.mediproject.core.model.dur.durproduct.extendedreleasetablet.DurProductExReleaseTableSplitAttentionWrapper
import com.android.mediproject.core.model.dur.durproduct.pregnancy.DurProductPregnantWomanTabooResponse
import com.android.mediproject.core.model.dur.durproduct.pregnancy.DurProductPregnantWomanTabooWrapper
import com.android.mediproject.core.model.dur.durproduct.senior.DurProductSeniorCautionResponse
import com.android.mediproject.core.model.dur.durproduct.senior.DurProductSeniorCautionWrapper
import com.android.mediproject.core.model.dur.durproduct.specialtyagegroup.DurProductSpecialtyAgeGroupTabooResponse
import com.android.mediproject.core.model.dur.durproduct.specialtyagegroup.DurProductSpecialtyWrapper

class DurItemWrapperFactory {
    companion object {
        inline fun <reified T : DataGoKrResponse<*>> createForDurProduct(durType: DurType, response: T): DurItemWrapper = when (durType) {
            DurType.SPECIALTY_AGE_GROUP_TABOO -> DurProductSpecialtyWrapper(response as DurProductSpecialtyAgeGroupTabooResponse)
            DurType.SENIOR_CAUTION -> DurProductSeniorCautionWrapper(response as DurProductSeniorCautionResponse)
            DurType.DOSING_CAUTION -> DurProductDosingCautionWrapper(response as DurProductDosingCautionResponse)
            DurType.EFFICACY_GROUP_DUPLICATION -> DurProductEfficacyGroupDuplicationWrapper(response as DurProductEfficacyGroupDuplicationResponse)
            DurType.EX_RELEASE_TABLET_SPLIT_ATTENTION -> DurProductExReleaseTableSplitAttentionWrapper(response as DurProductExReleaseTableSplitAttentionResponse)
            DurType.COMBINATION_TABOO -> DurProductCombinationTabooWrapper(response as DurProductCombinationTabooResponse)
            DurType.PREGNANT_WOMAN_TABOO -> DurProductPregnantWomanTabooWrapper(response as DurProductPregnantWomanTabooResponse)
            DurType.CAPACITY_ATTENTION -> DurProductCapacityAttentionWrapper(response as DurProductCapacityAttentionResponse)
        }

    }
}
