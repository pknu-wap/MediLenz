package com.android.mediproject.feature.medicine

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BasicInfoType : Parcelable {
    EFFICACY_EFFECT, DOSAGE, MEDICINE_INFO, PRECAUTIONS
}