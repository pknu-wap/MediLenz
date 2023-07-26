package com.android.mediproject.core.model.navargs

data class RecallDisposalArgs(
    val product: String,
) : BaseNavArgs(RecallDisposalArgs::class.java.name)
