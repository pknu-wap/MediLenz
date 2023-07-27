package com.android.mediproject.core.model.dur.durproduct.senior

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 노인 주의
 *
 * @param itemName 품목명
 * @param prohibitContent 노인주의 내용
 */
data class DurProductSeniorCaution(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.SENIOR_CAUTION)

class DurProductSeniorCautionWrapper(
    override val response: DurProductSeniorCautionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductSeniorCaution(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }

}
