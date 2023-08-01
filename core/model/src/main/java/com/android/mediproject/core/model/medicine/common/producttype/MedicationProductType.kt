package com.android.mediproject.core.model.medicine.common.producttype

import androidx.annotation.StringRes
import com.android.mediproject.core.model.R
import com.android.mediproject.core.model.medicine.common.producttype.MedicationProductType.GENERAL
import com.android.mediproject.core.model.medicine.common.producttype.MedicationProductType.SPECIALTY

/**
 * 의약품 타입
 *
 * @property SPECIALTY 전문의약품
 * @property GENERAL 일반의약품
 */
enum class MedicationProductType(val description: String, @StringRes val stringResId: Int) {
    SPECIALTY("전문", R.string.medicineProductType_specialty),
    GENERAL("일반", R.string.medicineProductType_general);

    companion object {
        fun enumOf(description: String) = values().find { description.contains(it.name) } ?: throw IllegalArgumentException()
    }
}
