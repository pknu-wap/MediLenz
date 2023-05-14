package com.android.mediproject.core.model.local.navargs

import com.android.mediproject.core.annotation.DeepLinkNavArgs

@DeepLinkNavArgs
data class MedicineInfoArgs(val medicineName: String, val imgUrl: String, val entpName: String, val itemSequence: String) :
    BaseNavArgs() {
}