package com.android.mediproject.core.model.dur.durproduct.combination

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

data class DurProductCombinationTaboo(
    val itemName: String,
    val ingrKorName: String,
    val ingrEngName: String,
    val className: String,
    val mixtureClassName: String,
    val mixtureIngrKorName: String,
    val mixtureIngrEngName: String,
    val remark: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.COMBINATION_TABOO)

class DurProductCombinationTabooWrapper(
    override val response: DurProductCombinationTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductCombinationTaboo(
            itemName = it.itemName,
            ingrKorName = it.ingrKorName,
            ingrEngName = it.ingrEngName,
            className = it.className,
            mixtureClassName = it.mixtureClassName,
            mixtureIngrKorName = it.mixtureIngrKorName,
            mixtureIngrEngName = it.mixtureIngrEngName,
            remark = it.remark,
            prohibitContent = it.prohibitContent,
        )
    }

}
