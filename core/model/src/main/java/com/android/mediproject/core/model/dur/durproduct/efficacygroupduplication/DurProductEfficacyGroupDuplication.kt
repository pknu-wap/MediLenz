package com.android.mediproject.core.model.dur.durproduct.efficacygroupduplication

import com.android.mediproject.core.model.dur.DurItemWrapper
import com.android.mediproject.core.model.dur.DurType
import com.android.mediproject.core.model.dur.durproduct.ui.DurProductItem

data class DurProductEfficacyGroupDuplication(
    val itemName: String,
    val effectName: String,
    val className: String,
    val ingrKorName: String,
    val ingrEngName: String,
    override val prohibitContent: String,
) : DurProductItem(DurType.EFFICACY_GROUP_DUPLICATION)

class DurProductEfficacyGroupDuplicationWrapper(
    override val response: DurProductEfficacyGroupDuplicationResponse,
) : DurItemWrapper(response) {
    override fun convert() = response.body.items.map {
        DurProductEfficacyGroupDuplication(
            itemName = it.itemName,
            effectName = it.effectName,
            className = it.className,
            ingrKorName = it.ingrName,
            ingrEngName = it.ingrEngName,
            prohibitContent = it.prohibitContent,
        )
    }
}
