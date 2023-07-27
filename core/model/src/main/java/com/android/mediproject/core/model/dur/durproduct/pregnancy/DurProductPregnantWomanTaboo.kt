package com.android.mediproject.core.model.dur.durproduct.pregnancy

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

/**
 * 임부 금기
 *
 * @param itemName 품목명
 * @param ingrKorName 성분명(한글)
 * @param ingrEngName 성분명(영문)
 * @param prohibitContent 금기 내용
 */
data class DurProductPregnantWomanTaboo(
    val itemName: String,
    val ingrKorName: String,
    val ingrEngName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.PREGNANT_WOMAN_TABOO)

class DurProductPregnantWomanTabooWrapper(
    override val response: DurProductPregnantWomanTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductPregnantWomanTaboo(
            itemName = it.itemName,
            ingrKorName = it.ingrName,
            ingrEngName = it.ingrEngName,
            prohibitContent = it.prohibitContent,
        )
    }

}
