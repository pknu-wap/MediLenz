package com.android.mediproject.core.model.medicine.common.producttype

import androidx.annotation.StringRes
import com.android.mediproject.core.model.R
import com.android.mediproject.core.model.medicine.common.producttype.FilterMedicationProductType.ALL
import com.android.mediproject.core.model.medicine.common.producttype.FilterMedicationProductType.GENERAL
import com.android.mediproject.core.model.medicine.common.producttype.FilterMedicationProductType.SPECIALTY

/**
 * 표시할 의약품 타입
 *
 * @property SPECIALTY 전문의약품
 * @property GENERAL 일반의약품
 * @property ALL 전체
 */
enum class FilterMedicationProductType(val text: String, @StringRes val stringResId: Int) {
    ALL("", R.string.medicineProductType_all), SPECIALTY("전문", R.string.medicineProductType_specialty), GENERAL(
        "일반",
        R.string.medicineProductType_general,
    );

    companion object {
        fun FilterMedicationProductType.text() = text
        fun FilterMedicationProductType.stringResId() = stringResId

        fun enumOf(text: String) = values().find { text.contains(it.text) }!!
    }
}
