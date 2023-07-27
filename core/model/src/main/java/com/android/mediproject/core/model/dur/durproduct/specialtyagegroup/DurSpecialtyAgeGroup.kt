package com.android.mediproject.core.model.dur.durproduct.specialtyagegroup

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 특정연령대 금기
 *
 * @param itemName 품목명
 * @param prohibitContent 연령금기내용
 */
data class DurSpecialtyAgeGroup(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.SPECIALTY_AGE_GROUP_TABOO)

class DurProductSpecialtyWrapper(
    override val response: DurProductSpecialtyAgeGroupTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurSpecialtyAgeGroup(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }

}
