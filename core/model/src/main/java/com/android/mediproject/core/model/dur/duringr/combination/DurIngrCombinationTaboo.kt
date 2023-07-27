package com.android.mediproject.core.model.dur.duringr.combination

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.duringr.ui.DurIngrItem

data class DurIngrCombinationTaboo(
    val className: String,
    val ingrKorName: String,
    val ingrEngName: String,
    val mixtureClassName: String,
    val mixtureIngrKorName: String,
    val mixtureIngrEngName: String,
    val remark: String,
    override val prohibitContent: String = "",
) : DurIngrItem(DurType.COMBINATION_TABOO)

class DurIngrCombinationTabooWrapper(
    override val response: DurIngrCombinationTabooResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        val item = it.item
        DurIngrCombinationTaboo(
            className = item.className,
            ingrKorName = item.ingrKorName,
            ingrEngName = item.ingrEngName,
            mixtureClassName = item.mixtureClass,
            mixtureIngrKorName = item.mixtureIngrKorName,
            mixtureIngrEngName = item.mixtureIngrEngName,
            remark = item.remark,
            prohibitContent = item.prohibitContent,
        )
    }

}
