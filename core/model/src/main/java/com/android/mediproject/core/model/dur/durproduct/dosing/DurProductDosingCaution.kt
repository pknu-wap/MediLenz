package com.android.mediproject.core.model.dur.durproduct.dosing

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 투여기간 주의
 *
 * @param itemName 품목명
 * @param prohibitContent 최대 투여기간
 */
data class DurProductDosingCaution(
    val itemName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.DOSING_CAUTION)


class DurProductDosingCautionWrapper(
    override val response: DurProductDosingCautionResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductDosingCaution(
            itemName = it.itemName,
            prohibitContent = it.prohibitContent,
        )
    }
}
